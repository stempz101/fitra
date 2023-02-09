package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.invitation.InvitationDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.model.Invitation;
import com.diploma.fitra.model.Participant;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.enums.InvitationStatus;
import com.diploma.fitra.model.enums.Role;
import com.diploma.fitra.repo.InvitationRepository;
import com.diploma.fitra.repo.ParticipantRepository;
import com.diploma.fitra.repo.TravelRepository;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.test.util.InvitationDataTest;
import com.diploma.fitra.test.util.ParticipantDataTest;
import com.diploma.fitra.test.util.TravelDataTest;
import com.diploma.fitra.test.util.UserDataTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvitationServiceImplTest {

    @InjectMocks
    private InvitationServiceImpl invitationService;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private TravelRepository travelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Test
    void createInvitationTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);
        List<Invitation> invitations = new ArrayList<>();
        invitations.add(InvitationDataTest.getInvitation2());
        invitations.add(InvitationDataTest.getInvitation1());
        invitations.add(InvitationDataTest.getInvitation3());
        Invitation invitation = InvitationDataTest.getInvitation4();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        when(participantRepository.findById(any())).thenReturn(Optional.empty());
        when(invitationRepository.findAllByTravelAndUser(any(), any())).thenReturn(invitations);
        when(invitationRepository.save(any())).thenReturn(invitation);
        invitationService.createInvitation(travel.getId(), user.getId(), auth);

        verify(invitationRepository, times(1)).save(any());
    }

    @Test
    void createInvitationWithTravelNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> invitationService.createInvitation(travel.getId(), user.getId(), auth));
    }

    @Test
    void createInvitationWithUserNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> invitationService.createInvitation(travel.getId(), user.getId(), auth));
    }

    @Test
    void createInvitationWithForbiddenExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);

        assertThrows(ForbiddenException.class, () -> invitationService.createInvitation(travel.getId(), user.getId(), auth));
    }

    @Test
    void createInvitationWithBadRequestExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        user.setRole(Role.ADMIN);
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());

        assertThrows(BadRequestException.class, () -> invitationService.createInvitation(travel.getId(), user.getId(), auth));
    }

    @Test
    void createInvitationWithParticipantExistenceExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);
        Participant participant = ParticipantDataTest.getParticipant2();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        when(participantRepository.findById(any())).thenReturn(Optional.of(participant));

        assertThrows(ExistenceException.class, () -> invitationService.createInvitation(travel.getId(), user.getId(), auth));
    }

    @Test
    void createInvitationWithInvitationExistenceExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);
        List<Invitation> invitations = new ArrayList<>();
        invitations.add(InvitationDataTest.getInvitation2());
        invitations.add(InvitationDataTest.getInvitation4());
        invitations.add(InvitationDataTest.getInvitation3());

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        when(participantRepository.findById(any())).thenReturn(Optional.empty());
        when(invitationRepository.findAllByTravelAndUser(any(), any())).thenReturn(invitations);

        assertThrows(ExistenceException.class, () -> invitationService.createInvitation(travel.getId(), user.getId(), auth));
    }

    @Test
    void getInvitationsTest() {
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);
        List<Invitation> invitations = new ArrayList<>();
        invitations.add(InvitationDataTest.getInvitation4());
        invitations.add(InvitationDataTest.getInvitation3());
        invitations.add(InvitationDataTest.getInvitation2());
        invitations.add(InvitationDataTest.getInvitation1());

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(invitationRepository.findAllByUser(any())).thenReturn(invitations);
        List<InvitationDto> result = invitationService.getInvitations(user.getId(), auth);

        assertThat(result, hasSize(invitations.size()));
        assertThat(result, hasItems(
                InvitationDataTest.getInvitationDto4(),
                InvitationDataTest.getInvitationDto3(),
                InvitationDataTest.getInvitationDto2(),
                InvitationDataTest.getInvitationDto1()
        ));
    }

    @Test
    void getInvitationsWithUserNotFoundExceptionTest() {
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> invitationService.getInvitations(user.getId(), auth));
    }

    @Test
    void getInvitationsWithForbiddenExceptionTest() {
        User user2 = UserDataTest.getUser2();
        User user3 = UserDataTest.getUser3();
        Authentication auth = mock(Authentication.class);

        when(userRepository.findById(any())).thenReturn(Optional.of(user2));
        when(auth.getPrincipal()).thenReturn(user3);

        assertThrows(ForbiddenException.class, () -> invitationService.getInvitations(user2.getId(), auth));
    }

    @Test
    void getInvitationsForCreatorTest() {
        User user = UserDataTest.getUser1();
        Authentication auth = mock(Authentication.class);
        List<Invitation> invitations = new ArrayList<>();
        invitations.add(InvitationDataTest.getInvitation4());
        invitations.add(InvitationDataTest.getInvitation3());
        invitations.add(InvitationDataTest.getInvitation2());
        invitations.add(InvitationDataTest.getInvitation1());

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(invitationRepository.findAllByTravel_Creator(any())).thenReturn(invitations);
        List<InvitationDto> result = invitationService.getInvitationsForCreator(user.getId(), auth);

        assertThat(result, hasSize(invitations.size()));
        assertThat(result, hasItems(
                InvitationDataTest.getInvitationDto4(),
                InvitationDataTest.getInvitationDto3(),
                InvitationDataTest.getInvitationDto2(),
                InvitationDataTest.getInvitationDto1()
        ));
    }

    @Test
    void getInvitationsForCreatorWithUserNotFoundExceptionTest() {
        User user = UserDataTest.getUser1();
        Authentication auth = mock(Authentication.class);

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> invitationService.getInvitationsForCreator(user.getId(), auth));
    }

    @Test
    void getInvitationsForCreatorWithForbiddenExceptionTest() {
        User user1 = UserDataTest.getUser1();
        User user2 = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);

        when(userRepository.findById(any())).thenReturn(Optional.of(user1));
        when(auth.getPrincipal()).thenReturn(user2);

        assertThrows(ForbiddenException.class, () -> invitationService.getInvitationsForCreator(user1.getId(), auth));
    }

    @Test
    void confirmInvitationTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        Authentication auth = mock(Authentication.class);
        Participant participant = ParticipantDataTest.getParticipant2();
        Invitation confirmedInvitation = InvitationDataTest.getInvitation4();
        confirmedInvitation.setStatus(InvitationStatus.CONFIRMED);

        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        when(auth.getPrincipal()).thenReturn(invitation.getUser());
        when(participantRepository.save(any())).thenReturn(participant);
        when(invitationRepository.save(any())).thenReturn(confirmedInvitation);
        invitationService.confirmInvitation(invitation.getId(), auth);

        verify(participantRepository, times(1)).save(any());
        verify(invitationRepository, times(1)).save(any());
    }

    @Test
    void confirmInvitationWithNotFoundExceptionTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> invitationService.confirmInvitation(invitation.getId(), auth));
    }

    @Test
    void confirmInvitationWithForbiddenExceptionTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        when(auth.getPrincipal()).thenReturn(invitation.getTravel().getCreator());

        assertThrows(ForbiddenException.class, () -> invitationService.confirmInvitation(invitation.getId(), auth));
    }

    @Test
    void confirmInvitationsWithConfirmedStatusBadRequestExceptionTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        invitation.setStatus(InvitationStatus.CONFIRMED);
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        when(auth.getPrincipal()).thenReturn(invitation.getUser());

        assertThrows(BadRequestException.class, () -> invitationService.confirmInvitation(invitation.getId(), auth));
    }

    @Test
    void confirmInvitationsWithRejectedStatusBadRequestExceptionTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        invitation.setStatus(InvitationStatus.REJECTED);
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        when(auth.getPrincipal()).thenReturn(invitation.getUser());

        assertThrows(BadRequestException.class, () -> invitationService.confirmInvitation(invitation.getId(), auth));
    }

    @Test
    void rejectInvitationTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        Authentication auth = mock(Authentication.class);
        Invitation rejectedInvitation = InvitationDataTest.getInvitation4();
        rejectedInvitation.setStatus(InvitationStatus.REJECTED);

        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        when(auth.getPrincipal()).thenReturn(invitation.getUser());
        when(invitationRepository.save(any())).thenReturn(rejectedInvitation);
        invitationService.rejectInvitation(invitation.getId(), auth);

        verify(invitationRepository, times(1)).save(any());
    }

    @Test
    void rejectInvitationWithNotFoundExceptionTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> invitationService.rejectInvitation(invitation.getId(), auth));
    }

    @Test
    void rejectInvitationWithForbiddenExceptionTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        when(auth.getPrincipal()).thenReturn(invitation.getTravel().getCreator());

        assertThrows(ForbiddenException.class, () -> invitationService.rejectInvitation(invitation.getId(), auth));
    }

    @Test
    void rejectInvitationWithRejectedStatusBadRequestExceptionTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        invitation.setStatus(InvitationStatus.REJECTED);
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        when(auth.getPrincipal()).thenReturn(invitation.getUser());

        assertThrows(BadRequestException.class, () -> invitationService.rejectInvitation(invitation.getId(), auth));
    }

    @Test
    void rejectInvitationWithConfirmedStatusBadRequestExceptionTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        invitation.setStatus(InvitationStatus.CONFIRMED);
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        when(auth.getPrincipal()).thenReturn(invitation.getUser());

        assertThrows(BadRequestException.class, () -> invitationService.rejectInvitation(invitation.getId(), auth));
    }

    @Test
    void cancelInvitationTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        when(auth.getPrincipal()).thenReturn(invitation.getTravel().getCreator());
        invitationService.cancelInvitation(invitation.getId(), auth);

        verify(invitationRepository, times(1)).delete(any());
    }

    @Test
    void cancelInvitationWithNotFoundExceptionTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> invitationService.cancelInvitation(invitation.getId(), auth));
    }

    @Test
    void cancelInvitationWithForbiddenExceptionTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        when(auth.getPrincipal()).thenReturn(invitation.getUser());

        assertThrows(ForbiddenException.class, () -> invitationService.cancelInvitation(invitation.getId(), auth));
    }

    @Test
    void cancelInvitationWithConfirmedStatusBadRequestExceptionTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        invitation.setStatus(InvitationStatus.CONFIRMED);
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        when(auth.getPrincipal()).thenReturn(invitation.getTravel().getCreator());

        assertThrows(BadRequestException.class, () -> invitationService.cancelInvitation(invitation.getId(), auth));
    }

    @Test
    void cancelInvitationWithRejectedStatusBadRequestExceptionTest() {
        Invitation invitation = InvitationDataTest.getInvitation4();
        invitation.setStatus(InvitationStatus.REJECTED);
        Authentication auth = mock(Authentication.class);

        when(invitationRepository.findById(any())).thenReturn(Optional.of(invitation));
        when(auth.getPrincipal()).thenReturn(invitation.getTravel().getCreator());

        assertThrows(BadRequestException.class, () -> invitationService.cancelInvitation(invitation.getId(), auth));
    }
}
