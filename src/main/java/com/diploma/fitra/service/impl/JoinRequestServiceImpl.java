package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.RequestMapper;
import com.diploma.fitra.model.JoinRequest;
import com.diploma.fitra.model.Participant;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.enums.Role;
import com.diploma.fitra.model.enums.Status;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.model.key.ParticipantKey;
import com.diploma.fitra.repo.ParticipantRepository;
import com.diploma.fitra.repo.JoinRequestRepository;
import com.diploma.fitra.repo.TravelRepository;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.service.JoinRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
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
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    @Override
    public void createRequest(Long travelId, Long userId, Authentication auth) {
        log.info("Creating request to join into the travel (id={}) by user (id={})", travelId, userId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!user.getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (user.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException(Error.ADMIN_CANT_BE_ADDED_TO_TRAVEL.getMessage());
        } else if (participantRepository.findById(new ParticipantKey(travel.getId(), user.getId())).isPresent()) {
            throw new ExistenceException(Error.USER_EXISTS_IN_TRAVEL.getMessage());
        }

        List<JoinRequest> requests = requestRepository
                .findAllByTravelAndUser(travel, user, Sort.by("createTime").descending());
        if (requests.size() != 0 && requests.get(0).getStatus().equals(Status.PENDING)) {
            throw new ExistenceException(Error.REQUEST_IS_PENDING.getMessage());
        }

        JoinRequest request = new JoinRequest();
        request.setTravel(travel);
        request.setUser(user);
        request.setStatus(Status.PENDING);
        request.setCreateTime(LocalDateTime.now());
        requestRepository.save(request);

        log.info("Request to join into the travel (id={}) is created by user (id={})", travelId, userId);
    }

    @Override
    public List<RequestDto> getRequests(Long creatorId, Authentication auth) {
        log.info("Getting requests for creator (id={})", creatorId);

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!creator.getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        return requestRepository.findAllByTravel_Creator(creator).stream()
                .map(RequestMapper.INSTANCE::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getRequestsForUser(Long userId, Authentication auth) {
        log.info("Getting requests for user (id={})", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!user.getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        return requestRepository.findAllByUser(user).stream()
                .map(RequestMapper.INSTANCE::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void approveRequest(Long requestId, Authentication auth) {
        log.info("Request (id={}) confirmation", requestId);

        JoinRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(Error.REQUEST_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!request.getTravel().getCreator().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (request.getStatus().equals(Status.APPROVED)) {
            throw new BadRequestException(Error.REQUEST_IS_APPROVED.getMessage());
        } else if (request.getStatus().equals(Status.REJECTED)) {
            throw new BadRequestException(Error.REQUEST_IS_REJECTED.getMessage());
        }

        Participant participant = new Participant();
        participant.setTravel(request.getTravel());
        participant.setUser(request.getUser());
        participantRepository.save(participant);

        request.setStatus(Status.APPROVED);
        requestRepository.save(request);

        log.info("Request (id={}) to join into the travel (id={}) is confirmed successfully by the creator (id={})",
                requestId, request.getTravel().getId(), request.getTravel().getCreator().getId());
    }

    @Override
    public void rejectRequest(Long requestId, Authentication auth) {
        log.info("Request (id={}) rejection", requestId);

        JoinRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(Error.REQUEST_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!request.getTravel().getCreator().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (request.getStatus().equals(Status.REJECTED)) {
            throw new BadRequestException(Error.REQUEST_IS_REJECTED.getMessage());
        } else if (request.getStatus().equals(Status.APPROVED)) {
            throw new BadRequestException(Error.REQUEST_IS_APPROVED.getMessage());
        }

        request.setStatus(Status.REJECTED);
        requestRepository.save(request);

        log.info("Request (id={}) to join into the travel (id={}) is rejected successfully by the creator (id={})",
                requestId, request.getTravel().getId(), request.getTravel().getCreator().getId());
    }

    @Override
    public void cancelRequest(Long requestId, Authentication auth) {
        log.info("Request (id={}) cancellation", requestId);

        JoinRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(Error.REQUEST_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!request.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (request.getStatus().equals(Status.APPROVED) ||
                request.getStatus().equals(Status.REJECTED)) {
            throw new BadRequestException(Error.REQUEST_IS_APPROVED_OR_REJECTED.getMessage());
        }

        requestRepository.delete(request);

        log.info("Request (id={}) to join into the travel (id={}) is cancelled successfully by the user (id={})",
                requestId, request.getTravel().getId(), request.getUser().getId());
    }
}
