package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.invitation.InvitationDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.InvitationMapper;
import com.diploma.fitra.model.Invitation;
import com.diploma.fitra.model.Participant;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.enums.InvitationStatus;
import com.diploma.fitra.model.enums.Role;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.model.key.ParticipantKey;
import com.diploma.fitra.repo.InvitationRepository;
import com.diploma.fitra.repo.ParticipantRepository;
import com.diploma.fitra.repo.TravelRepository;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.service.InvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final TravelRepository travelRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    @Override
    public void createInvitation(Long travelId, Long userId, Authentication auth) {
        log.info("Creating travel (id={}) invitation to the user (id={})", travelId, userId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!travel.getCreator().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (user.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException(Error.ADMIN_CANT_BE_ADDED_TO_TRAVEL.getMessage());
        } else if (participantRepository.findById(new ParticipantKey(travel.getId(), user.getId())).isPresent()) {
            throw new ExistenceException(Error.USER_EXISTS_IN_TRAVEL.getMessage());
        }

        List<Invitation> invitations = invitationRepository.findAllByTravelAndUser(travel, user).stream()
                .sorted((inv1, inv2) -> inv2.getCreateTime().compareTo(inv1.getCreateTime()))
                .collect(Collectors.toList());
        if (invitations.size() != 0 && invitations.get(0).getStatus().equals(InvitationStatus.WAITING)) {
            throw new ExistenceException(Error.INVITATION_IS_WAITING.getMessage());
        }

        Invitation invitation = new Invitation();
        invitation.setTravel(travel);
        invitation.setUser(user);
        invitation.setStatus(InvitationStatus.WAITING);
        invitation.setCreateTime(LocalDateTime.now());
        invitationRepository.save(invitation);

        log.info("Travel (id={}) invitation is created to the user (id={})", travelId, userId);
    }

    @Override
    public List<InvitationDto> getInvitations(Long userId, Authentication auth) {
        log.info("Getting invitations");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!user.getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        return invitationRepository.findAllByUser(user).stream()
                .map(InvitationMapper.INSTANCE::toInvitationDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvitationDto> getInvitationsForCreator(Long creatorId, Authentication auth) {
        log.info("Getting invitations for creator");

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!creator.getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        return invitationRepository.findAllByTravel_Creator(creator).stream()
                .map(InvitationMapper.INSTANCE::toInvitationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void confirmInvitation(Long invitationId, Authentication auth) {
        log.info("Invitation (id={}) confirmation", invitationId);

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException(Error.INVITATION_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!invitation.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (invitation.getStatus().equals(InvitationStatus.CONFIRMED)) {
            throw new BadRequestException(Error.INVITATION_IS_CONFIRMED.getMessage());
        } else if (invitation.getStatus().equals(InvitationStatus.REJECTED)) {
            throw new BadRequestException(Error.INVITATION_IS_REJECTED.getMessage());
        }

        Participant participant = new Participant();
        participant.setId(new ParticipantKey(invitation.getTravel().getId(), invitation.getUser().getId()));
        participant.setTravel(invitation.getTravel());
        participant.setUser(invitation.getUser());
        participantRepository.save(participant);

        invitation.setStatus(InvitationStatus.CONFIRMED);
        invitationRepository.save(invitation);

        log.info("Invitation (id={}) to the travel (id={}) is confirmed successfully by user with id={}",
                invitationId, invitation.getTravel().getId(), invitation.getUser().getId());
    }

    @Override
    public void rejectInvitation(Long invitationId, Authentication auth) {
        log.info("Invitation (id={}) rejection", invitationId);

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException(Error.INVITATION_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!invitation.getUser().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (invitation.getStatus().equals(InvitationStatus.REJECTED)) {
            throw new BadRequestException(Error.INVITATION_IS_REJECTED.getMessage());
        } else if (invitation.getStatus().equals(InvitationStatus.CONFIRMED)) {
            throw new BadRequestException(Error.INVITATION_IS_CONFIRMED.getMessage());
        }

        invitation.setStatus(InvitationStatus.REJECTED);
        invitationRepository.save(invitation);

        log.info("Invitation (id={}) to the travel (id={}) is rejected successfully by user with id={}",
                invitationId, invitation.getTravel().getId(), invitation.getUser().getId());
    }

    @Override
    public void cancelInvitation(Long invitationId, Authentication auth) {
        log.info("Invitation (id={}) cancellation", invitationId);

        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new NotFoundException(Error.INVITATION_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!invitation.getTravel().getCreator().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (invitation.getStatus().equals(InvitationStatus.CONFIRMED) ||
                invitation.getStatus().equals(InvitationStatus.REJECTED)) {
            throw new BadRequestException(Error.INVITATION_IS_CONFIRMED_OR_REJECTED.getMessage());
        }

        invitationRepository.delete(invitation);

        log.info("Invitation (id={}) to the travel (id={}) is cancelled successfully by creator with id={}",
                invitationId, invitation.getTravel().getId(), invitation.getTravel().getCreator().getId());
    }
}