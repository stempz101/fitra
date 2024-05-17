package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.invitation.InvitationDto;
import com.diploma.fitra.dto.invitation.InvitationSaveDto;
import com.diploma.fitra.dto.travel.RouteDto;
import com.diploma.fitra.dto.ws.InvitationsUnreadDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.InvitationMapper;
import com.diploma.fitra.mapper.RouteMapper;
import com.diploma.fitra.model.Invitation;
import com.diploma.fitra.model.Participant;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.enums.Role;
import com.diploma.fitra.model.enums.Status;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.model.key.ParticipantKey;
import com.diploma.fitra.repo.*;
import com.diploma.fitra.service.InvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final TravelRepository travelRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final RouteRepository routeRepository;

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void createInvitation(Long travelId, Long userId, InvitationSaveDto invitationSaveDto, UserDetails userDetails) {
        log.info("Creating travel (id={}) invitation to the user (id={})", travelId, userId);

        User creator = (User) userDetails;
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getId().equals(creator.getId())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (participantRepository.findById(new ParticipantKey(travel.getId(), user.getId())).isPresent()) {
            throw new ExistenceException(Error.USER_EXISTS_IN_TRAVEL.getMessage());
        }

        if (invitationRepository.existsByUserIdAndTravelIdAndStatus(userId, travelId, Status.PENDING)) {
            throw new ExistenceException(Error.INVITATION_IS_PENDING.getMessage());
        }

        Invitation invitation = new Invitation();
        invitation.setTravel(travel);
        invitation.setUser(user);
        if (invitationSaveDto.getText() != null && !invitationSaveDto.getText().isEmpty()) {
            invitation.setText(invitationSaveDto.getText());
        }
        invitation.setStatus(Status.PENDING);
        invitation.setCreateTime(LocalDateTime.now());
        invitationRepository.save(invitation);

        log.info("Travel (id={}) invitation is created to the user (id={})", travelId, userId);

        sendNotification(invitation.getUser().getId());
    }

    @Override
    public List<InvitationDto> getUserInvitations(Pageable pageable, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Getting invitations for user (id={})", user.getId());

        return invitationRepository.findAllByUserIdOrderByCreateTimeDesc(user.getId(), pageable).stream()
                .map(invitation -> {
                    InvitationDto invitationDto = InvitationMapper.INSTANCE.toInvitationDto(invitation);
                    invitationDto.getTravel().setRoute(getRouteDtoList(invitation.getTravel()));
                    return invitationDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<InvitationDto> getTravelInvitations(Long travelId, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Getting travel (id={}) invitations for creator (id={})", travelId, user.getId());

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getId().equals(user.getId())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        }

        return invitationRepository.findAllByTravelIdOrderByCreateTimeDesc(travelId).stream()
                .map(InvitationMapper.INSTANCE::toInvitationDto)
                .collect(Collectors.toList());
    }

    @Override
    public void setInviteAsViewed(Long invitationId, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("User (id={}) is viewing the invitation (id={})", user.getId(), invitationId);

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException(Error.INVITATION_NOT_FOUND.getMessage()));
        if (!invitation.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        invitation.setViewed(true);
        invitationRepository.save(invitation);

        sendNotification(invitation.getUser().getId());
    }

    @Override
    @Transactional
    public void approveInvitation(Long invitationId, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Invitation (id={}) confirmation by user (id={})", invitationId, user.getId());

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException(Error.INVITATION_NOT_FOUND.getMessage()));
        if (!invitation.getUser().getId().equals(user.getId())) {
            if (!user.getRole().equals(Role.ADMIN)) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        } else if (invitation.getStatus().equals(Status.APPROVED)) {
            throw new BadRequestException(Error.INVITATION_IS_APPROVED.getMessage());
        } else if (invitation.getStatus().equals(Status.REJECTED)) {
            throw new BadRequestException(Error.INVITATION_IS_REJECTED.getMessage());
        }

        Participant participant = new Participant();
        participant.setTravel(invitation.getTravel());
        participant.setUser(invitation.getUser());
        participantRepository.save(participant);

        invitation.setStatus(Status.APPROVED);
        invitationRepository.save(invitation);

        log.info("Invitation (id={}) to the travel (id={}) is confirmed successfully by the user with id={}",
                invitationId, invitation.getTravel().getId(), invitation.getUser().getId());
    }

    @Override
    public void rejectInvitation(Long invitationId, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Invitation (id={}) rejection by user (id={})", invitationId, user.getId());

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException(Error.INVITATION_NOT_FOUND.getMessage()));
        if (!invitation.getUser().getId().equals(user.getId())) {
            if (!user.getRole().equals(Role.ADMIN)) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        } else if (invitation.getStatus().equals(Status.REJECTED)) {
            throw new BadRequestException(Error.INVITATION_IS_REJECTED.getMessage());
        } else if (invitation.getStatus().equals(Status.APPROVED)) {
            throw new BadRequestException(Error.INVITATION_IS_APPROVED.getMessage());
        }

        invitation.setStatus(Status.REJECTED);
        invitationRepository.save(invitation);

        log.info("Invitation (id={}) to the travel (id={}) is rejected successfully by the user with id={}",
                invitationId, invitation.getTravel().getId(), invitation.getUser().getId());
    }

    @Override
    public void deleteInvitation(Long invitationId, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Invitation (id={}) cancellation by user (id={})", invitationId, user.getId());

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException(Error.INVITATION_NOT_FOUND.getMessage()));
        if (!invitation.getTravel().getCreator().getId().equals(user.getId())) {
            if (!invitation.getUser().getId().equals(user.getId())) {
                if (!user.getRole().equals(Role.ADMIN)) {
                    throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
                }
            }
        }

        invitationRepository.delete(invitation);

        log.info("Invitation (id={}) to the travel (id={}) is cancelled successfully by the creator with id={}",
                invitationId, invitation.getTravel().getId(), invitation.getTravel().getCreator().getId());

        sendNotification(invitation.getUser().getId());
    }

    private List<RouteDto> getRouteDtoList(Travel travel) {
        return routeRepository.findAllByTravelIdOrderByPositionAsc(travel.getId())
                .stream()
                .map(RouteMapper.INSTANCE::toRouteDto)
                .collect(Collectors.toList());
    }

    private void sendNotification(Long userId) {
        long count = invitationRepository.countByUserIdAndViewedIsFalse(userId);
        messagingTemplate.convertAndSend("/unread-topic/invitations/user/" + userId,
                InvitationsUnreadDto.builder()
                        .invitationsCount(count)
                        .build());
    }
}
