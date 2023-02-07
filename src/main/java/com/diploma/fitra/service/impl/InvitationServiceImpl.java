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
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.model.key.InvitationKey;
import com.diploma.fitra.model.key.ParticipantKey;
import com.diploma.fitra.model.enums.Role;
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
    @Transactional
    public void confirmInvitation(Long travelId, Long userId, Authentication auth) {
        log.info("Invite confirmation to the travel (id={}) by user with id={}", travelId, userId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        Invitation invitation = invitationRepository.findById(new InvitationKey(travel.getId(), user.getId()))
                .orElseThrow(() -> new NotFoundException(Error.INVITATION_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!user.getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (participantRepository.findById(new ParticipantKey(travel.getId(), user.getId())).isPresent()) {
            throw new ExistenceException(Error.USER_EXISTS_IN_TRAVEL.getMessage());
        }

        Participant participant = new Participant();
        participant.setId(new ParticipantKey(travel.getId(), user.getId()));
        participant.setTravel(travel);
        participant.setUser(user);
        participantRepository.save(participant);

        invitation.setStatus(InvitationStatus.CONFIRMED);
        invitationRepository.save(invitation);

        log.info("Invite to the travel (id={}) was confirmed successfully by user with id={}", travelId, userId);
    }

    @Override
    public void rejectInvitation(Long travelId, Long userId, Authentication auth) {

    }

    @Override
    public void cancelInvitation(Long travelId, Long userId, Authentication auth) {

    }
}
