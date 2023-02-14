package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.model.*;
import com.diploma.fitra.model.enums.Status;
import com.diploma.fitra.repo.ParticipantRepository;
import com.diploma.fitra.repo.RequestRepository;
import com.diploma.fitra.repo.TravelRepository;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.test.util.*;
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
public class RequestServiceImplTest {

    @InjectMocks
    private RequestServiceImpl requestService;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private TravelRepository travelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Test
    void createRequestTest() {
        Travel travel = TravelDataTest.getTravel3();
        User user = UserDataTest.getUser3();
        Authentication auth = mock(Authentication.class);
        List<Request> requests = new ArrayList<>();
        requests.add(RequestDataTest.getRequest3());
        requests.add(RequestDataTest.getRequest2());
        requests.add(RequestDataTest.getRequest1());
        Request request = RequestDataTest.getRequest4();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(participantRepository.findById(any())).thenReturn(Optional.empty());
        when(requestRepository.findAllByTravelAndUser(any(), any(), any())).thenReturn(requests);
        when(requestRepository.save(any())).thenReturn(request);
        requestService.createRequest(travel.getId(), user.getId(), auth);

        verify(requestRepository, times(1)).save(any());
    }

    @Test
    void createRequestWithTravelNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel3();
        User user = UserDataTest.getUser3();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.createRequest(travel.getId(), user.getId(), auth));
    }

    @Test
    void createRequestWithUserNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel3();
        User user = UserDataTest.getUser3();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.createRequest(travel.getId(), user.getId(), auth));
    }

    @Test
    void createRequestWithForbiddenExceptionTest() {
        Travel travel = TravelDataTest.getTravel3();
        User user = UserDataTest.getUser3();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());

        assertThrows(ForbiddenException.class, () -> requestService.createRequest(travel.getId(), user.getId(), auth));
    }

    @Test
    void createRequestWithBadRequestExceptionTest() {
        Travel travel = TravelDataTest.getTravel3();
        User user = UserDataTest.getUser1();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);

        assertThrows(BadRequestException.class, () -> requestService.createRequest(travel.getId(), user.getId(), auth));
    }

    @Test
    void createRequestWithParticipantExistenceExceptionTest() {
        Travel travel = TravelDataTest.getTravel3();
        User user = UserDataTest.getUser3();
        Authentication auth = mock(Authentication.class);
        Participant participant = ParticipantDataTest.getParticipant3();
        participant.setTravel(travel);

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(participantRepository.findById(any())).thenReturn(Optional.of(participant));

        assertThrows(ExistenceException.class, () -> requestService.createRequest(travel.getId(), user.getId(), auth));
    }

    @Test
    void createRequestWithRequestExistenceExceptionTest() {
        Travel travel = TravelDataTest.getTravel3();
        User user = UserDataTest.getUser3();
        Authentication auth = mock(Authentication.class);
        List<Request> requests = new ArrayList<>();
        requests.add(RequestDataTest.getRequest4());
        requests.add(RequestDataTest.getRequest3());
        requests.add(RequestDataTest.getRequest2());

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(participantRepository.findById(any())).thenReturn(Optional.empty());
        when(requestRepository.findAllByTravelAndUser(any(), any(), any())).thenReturn(requests);

        assertThrows(ExistenceException.class, () -> requestService.createRequest(travel.getId(), user.getId(), auth));
    }

    @Test
    void getRequestsTest() {
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);
        List<Request> requests = new ArrayList<>();
        requests.add(RequestDataTest.getRequest4());
        requests.add(RequestDataTest.getRequest3());
        requests.add(RequestDataTest.getRequest2());
        requests.add(RequestDataTest.getRequest1());

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(requestRepository.findAllByTravel_Creator(any())).thenReturn(requests);
        List<RequestDto> result = requestService.getRequests(user.getId(), auth);

        assertThat(result, hasSize(requests.size()));
        assertThat(result, hasItems(
                RequestDataTest.getRequestDto4(),
                RequestDataTest.getRequestDto3(),
                RequestDataTest.getRequestDto2(),
                RequestDataTest.getRequestDto1()
        ));
    }

    @Test
    void getRequestsWithUserNotFoundExceptionTest() {
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getRequests(user.getId(), auth));
    }

    @Test
    void getRequestsWithForbiddenExceptionTest() {
        User user1 = UserDataTest.getUser2();
        User user2 = UserDataTest.getUser3();
        Authentication auth = mock(Authentication.class);

        when(userRepository.findById(any())).thenReturn(Optional.of(user1));
        when(auth.getPrincipal()).thenReturn(user2);

        assertThrows(ForbiddenException.class, () -> requestService.getRequests(user1.getId(), auth));
    }

    @Test
    void getRequestsForUserTest() {
        User user = UserDataTest.getUser3();
        Authentication auth = mock(Authentication.class);
        List<Request> requests = new ArrayList<>();
        requests.add(RequestDataTest.getRequest4());
        requests.add(RequestDataTest.getRequest3());
        requests.add(RequestDataTest.getRequest2());
        requests.add(RequestDataTest.getRequest1());

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(requestRepository.findAllByUser(any())).thenReturn(requests);
        List<RequestDto> result = requestService.getRequestsForUser(user.getId(), auth);

        assertThat(result, hasSize(requests.size()));
        assertThat(result, hasItems(
                RequestDataTest.getRequestDto4(),
                RequestDataTest.getRequestDto3(),
                RequestDataTest.getRequestDto2(),
                RequestDataTest.getRequestDto1()
        ));
    }

    @Test
    void getRequestsForUserWithUserNotFoundExceptionTest() {
        User user = UserDataTest.getUser3();
        Authentication auth = mock(Authentication.class);

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.getRequestsForUser(user.getId(), auth));
    }

    @Test
    void getRequestsForUserWithForbiddenExceptionTest() {
        User user1 = UserDataTest.getUser3();
        User user2 = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);

        when(userRepository.findById(any())).thenReturn(Optional.of(user1));
        when(auth.getPrincipal()).thenReturn(user2);

        assertThrows(ForbiddenException.class, () -> requestService.getRequestsForUser(user1.getId(), auth));
    }

    @Test
    void confirmRequestTest() {
        Request request = RequestDataTest.getRequest4();
        Authentication auth = mock(Authentication.class);
        Participant participant = ParticipantDataTest.getParticipant3();
        participant.setTravel(request.getTravel());
        Request confirmedRequest = RequestDataTest.getRequest4();
        confirmedRequest.setStatus(Status.CONFIRMED);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(auth.getPrincipal()).thenReturn(request.getTravel().getCreator());
        when(participantRepository.save(any())).thenReturn(participant);
        when(requestRepository.save(any())).thenReturn(confirmedRequest);
        requestService.confirmRequest(request.getId(), auth);

        verify(participantRepository, times(1)).save(any());
        verify(requestRepository, times(1)).save(any());
    }

    @Test
    void confirmRequestWithNotFoundExceptionTest() {
        Request request = RequestDataTest.getRequest4();
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.confirmRequest(request.getId(), auth));
    }

    @Test
    void confirmRequestWithForbiddenExceptionTest() {
        Request request = RequestDataTest.getRequest4();
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(auth.getPrincipal()).thenReturn(request.getUser());

        assertThrows(ForbiddenException.class, () -> requestService.confirmRequest(request.getId(), auth));
    }

    @Test
    void confirmRequestWithConfirmedStatusBadRequestExceptionTest() {
        Request request = RequestDataTest.getRequest4();
        request.setStatus(Status.CONFIRMED);
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(auth.getPrincipal()).thenReturn(request.getTravel().getCreator());

        assertThrows(BadRequestException.class, () -> requestService.confirmRequest(request.getId(), auth));
    }

    @Test
    void confirmRequestWithRejectedStatusBadRequestExceptionTest() {
        Request request = RequestDataTest.getRequest4();
        request.setStatus(Status.REJECTED);
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(auth.getPrincipal()).thenReturn(request.getTravel().getCreator());

        assertThrows(BadRequestException.class, () -> requestService.confirmRequest(request.getId(), auth));
    }

    @Test
    void rejectRequestTest() {
        Request request = RequestDataTest.getRequest4();
        Authentication auth = mock(Authentication.class);
        Request rejectedRequest = RequestDataTest.getRequest4();
        rejectedRequest.setStatus(Status.REJECTED);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(auth.getPrincipal()).thenReturn(request.getTravel().getCreator());
        when(requestRepository.save(any())).thenReturn(rejectedRequest);
        requestService.rejectRequest(request.getId(), auth);

        verify(requestRepository, times(1)).save(any());
    }

    @Test
    void rejectRequestWithNotFoundExceptionTest() {
        Request request = RequestDataTest.getRequest4();
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.rejectRequest(request.getId(), auth));
    }

    @Test
    void rejectRequestWithForbiddenExceptionTest() {
        Request request = RequestDataTest.getRequest4();
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(auth.getPrincipal()).thenReturn(request.getUser());

        assertThrows(ForbiddenException.class, () -> requestService.rejectRequest(request.getId(), auth));
    }

    @Test
    void rejectRequestWithRejectedStatusBadRequestExceptionTest() {
        Request request = RequestDataTest.getRequest4();
        request.setStatus(Status.REJECTED);
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(auth.getPrincipal()).thenReturn(request.getTravel().getCreator());

        assertThrows(BadRequestException.class, () -> requestService.rejectRequest(request.getId(), auth));
    }

    @Test
    void rejectRequestWithConfirmedStatusBadRequestExceptionTest() {
        Request request = RequestDataTest.getRequest4();
        request.setStatus(Status.CONFIRMED);
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(auth.getPrincipal()).thenReturn(request.getTravel().getCreator());

        assertThrows(BadRequestException.class, () -> requestService.rejectRequest(request.getId(), auth));
    }

    @Test
    void cancelRequestTest() {
        Request request = RequestDataTest.getRequest4();
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(auth.getPrincipal()).thenReturn(request.getUser());
        requestService.cancelRequest(request.getId(), auth);

        verify(requestRepository, times(1)).delete(any());
    }

    @Test
    void cancelRequestWithNotFoundExceptionTest() {
        Request request = RequestDataTest.getRequest4();
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> requestService.cancelRequest(request.getId(), auth));
    }

    @Test
    void cancelRequestWithForbiddenExceptionTest() {
        Request request = RequestDataTest.getRequest4();
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(auth.getPrincipal()).thenReturn(request.getTravel().getCreator());

        assertThrows(ForbiddenException.class, () -> requestService.cancelRequest(request.getId(), auth));
    }

    @Test
    void cancelRequestWithConfirmedStatusBadRequestExceptionTest() {
        Request request = RequestDataTest.getRequest4();
        request.setStatus(Status.CONFIRMED);
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(auth.getPrincipal()).thenReturn(request.getUser());

        assertThrows(BadRequestException.class, () -> requestService.cancelRequest(request.getId(), auth));
    }

    @Test
    void cancelRequestWithRejectedStatusBadRequestExceptionTest() {
        Request request = RequestDataTest.getRequest4();
        request.setStatus(Status.REJECTED);
        Authentication auth = mock(Authentication.class);

        when(requestRepository.findById(any())).thenReturn(Optional.of(request));
        when(auth.getPrincipal()).thenReturn(request.getUser());

        assertThrows(BadRequestException.class, () -> requestService.cancelRequest(request.getId(), auth));
    }
}
