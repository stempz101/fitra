package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.dto.request.RequestSaveDto;
import com.diploma.fitra.dto.travel.RouteDto;
import com.diploma.fitra.dto.ws.RequestsUnreadDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.RequestMapper;
import com.diploma.fitra.mapper.RouteMapper;
import com.diploma.fitra.model.JoinRequest;
import com.diploma.fitra.model.Participant;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.enums.Role;
import com.diploma.fitra.model.enums.Status;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.model.key.ParticipantKey;
import com.diploma.fitra.repo.*;
import com.diploma.fitra.service.JoinRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JoinRequestServiceImpl implements JoinRequestService {

    private final JoinRequestRepository requestRepository;
    private final TravelRepository travelRepository;
    private final ParticipantRepository participantRepository;
    private final RouteRepository routeRepository;

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void createRequest(Long travelId, RequestSaveDto requestSaveDto, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Creating request to join into the travel (id={}) by user (id={})", travelId, user.getId());

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (participantRepository.findById(new ParticipantKey(travel.getId(), user.getId())).isPresent()) {
            throw new ExistenceException(Error.USER_EXISTS_IN_TRAVEL.getMessage());
        }

        if (requestRepository.existsByUserIdAndTravelIdAndStatus(user.getId(), travel.getId(), Status.PENDING)) {
            throw new ExistenceException(Error.REQUEST_IS_PENDING.getMessage());
        }

        JoinRequest request = new JoinRequest();
        request.setTravel(travel);
        request.setUser(user);
        if (requestSaveDto.getText() != null && !requestSaveDto.getText().isEmpty()) {
            request.setText(requestSaveDto.getText());
        }
        request.setStatus(Status.PENDING);
        request.setCreateTime(LocalDateTime.now());
        requestRepository.save(request);

        log.info("Request to join into the travel (id={}) is created by user (id={})", travelId, user.getId());

        sendNotifications(travel.getCreator().getId(), travel.getId());
    }

    @Override
    public List<RequestDto> getTravelRequests(Long travelId, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Getting travel (id={}) requests for creator (id={})", travelId, user.getId());

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getId().equals(user.getId())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        return requestRepository.findAllByTravelIdOrderByCreateTime(travelId).stream()
                .map(RequestMapper.INSTANCE::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getUserRequests(Pageable pageable, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Getting requests for user (id={})", user.getId());

        return requestRepository.findAllByUserIdOrderByCreateTime(user.getId(), pageable).stream()
                .map(request -> {
                    RequestDto requestDto = RequestMapper.INSTANCE.toRequestDto(request);
                    requestDto.getTravel().setRoute(getRouteDtoList(request.getTravel()));
                    return requestDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void setRequestAsViewed(Long requestId, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Creator (id={}) is viewing the join request (id={})", user.getId(), requestId);

        JoinRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(Error.REQUEST_NOT_FOUND.getMessage()));
        if (!request.getTravel().getCreator().getId().equals(user.getId())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        request.setViewed(true);
        requestRepository.save(request);

        sendNotifications(request.getTravel().getCreator().getId(), request.getTravel().getId());
    }

    @Override
    @Transactional
    public void approveRequest(Long requestId, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Request (id={}) confirmation by creator (id={})", requestId, user.getId());

        JoinRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(Error.REQUEST_NOT_FOUND.getMessage()));
        if (!request.getTravel().getCreator().getId().equals(user.getId())) {
            if (!user.getRole().equals(Role.ADMIN)) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        } else if (request.getStatus().equals(Status.APPROVED)) {
            throw new BadRequestException(Error.REQUEST_IS_APPROVED.getMessage());
        } else if (request.getStatus().equals(Status.REJECTED)) {
            throw new BadRequestException(Error.REQUEST_IS_REJECTED.getMessage());
        }

        Participant participant = new Participant();
        participant.setTravel(request.getTravel());
        participant.setUser(request.getUser());
        participantRepository.save(participant);

        boolean notify = false;
        request.setStatus(Status.APPROVED);
        if (!request.isViewed()) {
            request.setViewed(true);
            notify = true;
        }
        requestRepository.save(request);

        log.info("Request (id={}) to join into the travel (id={}) is confirmed successfully by the creator (id={})",
                requestId, request.getTravel().getId(), request.getTravel().getCreator().getId());

        if (notify) {
            sendNotifications(request.getTravel().getCreator().getId(), request.getTravel().getId());
        }
    }

    @Override
    public void rejectRequest(Long requestId, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Request (id={}) rejection by creator (id={})", requestId, user.getId());

        JoinRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(Error.REQUEST_NOT_FOUND.getMessage()));
        if (!request.getTravel().getCreator().getId().equals(user.getId())) {
            if (!user.getRole().equals(Role.ADMIN)) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        } else if (request.getStatus().equals(Status.REJECTED)) {
            throw new BadRequestException(Error.REQUEST_IS_REJECTED.getMessage());
        } else if (request.getStatus().equals(Status.APPROVED)) {
            throw new BadRequestException(Error.REQUEST_IS_APPROVED.getMessage());
        }

        boolean notify = false;
        request.setStatus(Status.REJECTED);
        if (!request.isViewed()) {
            request.setViewed(true);
            notify = true;
        }
        requestRepository.save(request);

        log.info("Request (id={}) to join into the travel (id={}) is rejected successfully by the creator (id={})",
                requestId, request.getTravel().getId(), request.getTravel().getCreator().getId());

        if (notify) {
            sendNotifications(request.getTravel().getCreator().getId(), request.getTravel().getId());
        }
    }

    @Override
    public void deleteRequest(Long requestId, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Request (id={}) cancellation by user (id={})", requestId, user.getId());

        JoinRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(Error.REQUEST_NOT_FOUND.getMessage()));
        if (!request.getUser().getId().equals(user.getId())) {
            if (!request.getTravel().getCreator().getId().equals(user.getId())) {
                if (!user.getRole().equals(Role.ADMIN)) {
                    throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
                }
            }
        }

        requestRepository.delete(request);

        log.info("Request (id={}) to join into the travel (id={}) is cancelled successfully by the user (id={})",
                requestId, request.getTravel().getId(), request.getUser().getId());

        sendNotifications(request.getTravel().getCreator().getId(), request.getTravel().getId());
    }

    private List<RouteDto> getRouteDtoList(Travel travel) {
        return routeRepository.findAllByTravelIdOrderByPositionAsc(travel.getId())
                .stream()
                .map(RouteMapper.INSTANCE::toRouteDto)
                .collect(Collectors.toList());
    }

    private void sendNotifications(Long creatorTravelId, Long travelId) {
        long creatorTravelsRequestsCount = requestRepository.countByTravel_CreatorIdAndViewedIsFalse(creatorTravelId);
        long travelRequestsCount = requestRepository.countByTravelIdAndViewedIsFalse(travelId);

        messagingTemplate.convertAndSend("/unread-topic/user/" + creatorTravelId + "/travels/requests",
                RequestsUnreadDto.builder().requestsCount(creatorTravelsRequestsCount).build());
        messagingTemplate.convertAndSend("/unread-topic/travel/" + travelId + "/requests",
                RequestsUnreadDto.builder().requestsCount(travelRequestsCount).build());
    }
}
