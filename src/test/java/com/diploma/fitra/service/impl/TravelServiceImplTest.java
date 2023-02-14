package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import com.diploma.fitra.dto.user.UserShortDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.TravelMapper;
import com.diploma.fitra.model.*;
import com.diploma.fitra.repo.*;
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
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TravelServiceImplTest {

    @InjectMocks
    private TravelServiceImpl travelService;

    @Mock
    private TravelRepository travelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TypeRepository typeRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Test
    void createTravelTest() {
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser1();
        UserShortDto userShortDto = UserDataTest.getUserShortDto1();
        Authentication auth = mock(Authentication.class);
        Country country1 = CountryDataTest.getCountry1();
        Country country2 = CountryDataTest.getCountry1();
        Country country3 = CountryDataTest.getCountry2();
        City city1 = CityDataTest.getCity1();
        City city2 = CityDataTest.getCity2();
        Route route1 = RouteDataTest.getRoute1();
        Route route2 = RouteDataTest.getRoute2();
        Route route3 = RouteDataTest.getRoute3();
        Participant participant = ParticipantDataTest.getParticipant1();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(typeRepository.findById(any())).thenReturn(Optional.of(travel.getType()));
        when(travelRepository.save(any())).thenReturn(travel);
        when(countryRepository.findById(any()))
                .thenReturn(Optional.of(country1))
                .thenReturn(Optional.of(country2))
                .thenReturn(Optional.of(country3));
        when(cityRepository.findById(any()))
                .thenReturn(Optional.of(city1))
                .thenReturn(Optional.of(city2))
                .thenReturn(Optional.empty());
        when(routeRepository.save(any()))
                .thenReturn(route1)
                .thenReturn(route2)
                .thenReturn(route3);
        when(participantRepository.save(any())).thenReturn(participant);
        TravelDto result = travelService.createTravel(travelSaveDto, auth);

        assertThat(result, allOf(
                hasProperty("id", equalTo(travel.getId())),
                hasProperty("title", equalTo(travelSaveDto.getTitle())),
                hasProperty("type", allOf(
                        hasProperty("id", equalTo(travelSaveDto.getTypeId())),
                        hasProperty("name", equalTo(travel.getType().getNameEn()))
                )),
                hasProperty("description", equalTo(travelSaveDto.getDescription())),
                hasProperty("limit", equalTo(travelSaveDto.getLimit())),
                hasProperty("startDate", equalTo(travelSaveDto.getStartDate())),
                hasProperty("creator", equalTo(userShortDto)),
                hasProperty("route", hasItems(
                        RouteDataTest.getRouteDto1(),
                        RouteDataTest.getRouteDto2(),
                        RouteDataTest.getRouteDto3()
                ))
        ));
    }

    @Test
    void createTravelWithUserNotFoundExceptionTest() {
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Authentication auth = mock(Authentication.class);

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.createTravel(travelSaveDto, auth));
    }

    @Test
    void createTravelWithForbiddenExceptionTest() {
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Authentication auth = mock(Authentication.class);
        User user1 = UserDataTest.getUser1();
        User user2 = UserDataTest.getUser2();

        when(userRepository.findById(any())).thenReturn(Optional.of(user1));
        when(auth.getPrincipal()).thenReturn(user2);

        assertThrows(ForbiddenException.class, () -> travelService.createTravel(travelSaveDto, auth));
    }

    @Test
    void createTravelWithTypeNotFoundExceptionTest() {
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser1();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(typeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.createTravel(travelSaveDto, auth));
    }

    @Test
    void createTravelWithCountryNotFoundExceptionTest() {
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Authentication auth = mock(Authentication.class);
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser1();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(typeRepository.findById(any())).thenReturn(Optional.of(travel.getType()));
        when(countryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.createTravel(travelSaveDto, auth));
    }

    @Test
    void createTravelWithCityBadRequestExceptionTest() {
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Authentication auth = mock(Authentication.class);
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser1();
        Country country = CountryDataTest.getCountry2();
        City city = CityDataTest.getCity1();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(typeRepository.findById(any())).thenReturn(Optional.of(travel.getType()));
        when(countryRepository.findById(any())).thenReturn(Optional.of(country));
        when(cityRepository.findById(any())).thenReturn(Optional.of(city));

        assertThrows(BadRequestException.class, () -> travelService.createTravel(travelSaveDto, auth));
    }

    @Test
    void getTravelsTest() {
        List<Travel> travels = new ArrayList<>();
        travels.add(TravelDataTest.getTravel1());
        travels.add(TravelDataTest.getTravel2());
        travels.add(TravelDataTest.getTravel3());
        Route route1 = RouteDataTest.getRoute1();
        Route route2 = RouteDataTest.getRoute2();
        Route route3 = RouteDataTest.getRoute3();
        List<Route> routes = List.of(route1, route2, route3);


        when(routeRepository.findAllByTravel(any(), any()))
                .thenReturn(routes)
                .thenReturn(routes)
                .thenReturn(routes);
        when(travelRepository.findAll()).thenReturn(travels);
        List<TravelDto> result = travelService.getTravels();

        TravelDto travelDto1 = TravelDataTest.getTravelDto1();
        TravelDto travelDto2 = TravelDataTest.getTravelDto2();
        TravelDto travelDto3 = TravelDataTest.getTravelDto3();
        System.out.println(result.get(0).equals(travelDto1));
        System.out.println(result.get(1).equals(travelDto2));
        System.out.println(result.get(2).equals(travelDto3));

        assertThat(result, hasSize(travels.size()));
        assertThat(result, hasItems(
                TravelDataTest.getTravelDto1(),
                TravelDataTest.getTravelDto2(),
                TravelDataTest.getTravelDto3()
        ));
    }

    @Test
    void getUsersTest() {
        Travel travel = TravelDataTest.getTravel1();
        List<Participant> participants = new ArrayList<>();
        participants.add(ParticipantDataTest.getParticipant1());
        participants.add(ParticipantDataTest.getParticipant2());
        participants.add(ParticipantDataTest.getParticipant3());

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(participantRepository.findAllByTravel(any())).thenReturn(participants);
        List<ParticipantDto> result = travelService.getUsers(travel.getId());

        assertThat(result, hasSize(participants.size()));
        assertThat(result, hasItems(
                ParticipantDataTest.getParticipantDto1(),
                ParticipantDataTest.getParticipantDto2(),
                ParticipantDataTest.getParticipantDto3()
        ));
    }

    @Test
    void removeUserTest() {
        Travel travel = TravelDataTest.getTravel1();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser3();
        Participant participant = ParticipantDataTest.getParticipant3();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(participantRepository.findById(any())).thenReturn(Optional.of(participant));
        travelService.removeUser(travel.getId(), user.getId(), auth);

        verify(participantRepository, times(1)).delete(any());
    }

    @Test
    void removeUserWithTravelNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser3();

        when(travelRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.removeUser(travel.getId(), user.getId(), auth));
    }

    @Test
    void removeUserWithForbiddenExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser3();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(user);

        assertThrows(ForbiddenException.class, () -> travelService.removeUser(travel.getId(), user.getId(), auth));
    }

    @Test
    void removeUserWithUserNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser3();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.removeUser(travel.getId(), user.getId(), auth));
    }

    @Test
    void removeUserWithBadRequestExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser3();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        when(userRepository.findById(any())).thenReturn(Optional.of(travel.getCreator()));

        assertThrows(BadRequestException.class, () -> travelService.removeUser(travel.getId(), user.getId(), auth));
    }

    @Test
    void removeUserWithExistenceExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser3();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(participantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ExistenceException.class, () -> travelService.removeUser(travel.getId(), user.getId(), auth));
    }

    @Test
    void leaveTravelByUserTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);
        Participant participant = ParticipantDataTest.getParticipant2();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(participantRepository.findById(any())).thenReturn(Optional.of(participant));
        travelService.leaveTravel(travel.getId(), user.getId(), auth);

        verify(participantRepository, times(1)).delete(any());
        verify(travelRepository, times(0)).delete(any());
    }

    @Test
    void leaveTravelByCreatorTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser1();
        Authentication auth = mock(Authentication.class);
        Participant participant = ParticipantDataTest.getParticipant2();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(participantRepository.findById(any())).thenReturn(Optional.of(participant));
        travelService.leaveTravel(travel.getId(), user.getId(), auth);

        verify(participantRepository, times(1)).delete(any());
        verify(travelRepository, times(1)).delete(any());
    }

    @Test
    void leaveTravelWithTravelNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.leaveTravel(travel.getId(), user.getId(), auth));
    }

    @Test
    void leaveTravelWithUserNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.leaveTravel(travel.getId(), user.getId(), auth));
    }

    @Test
    void leaveTravelWithForbiddenExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());

        assertThrows(ForbiddenException.class, () -> travelService.leaveTravel(travel.getId(), user.getId(), auth));
    }

    @Test
    void leaveTravelWithParticipantNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(auth.getPrincipal()).thenReturn(user);
        when(participantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.leaveTravel(travel.getId(), user.getId(), auth));
    }

    @Test
    void updateTravelTest() {
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Travel travel = TravelDataTest.getTravel2();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser1();
        UserShortDto userShortDto = UserDataTest.getUserShortDto1();
        Type updatedType = TypeDataTest.getType1();
        Travel updatedTravel = TravelMapper.INSTANCE.fromTravelSaveDto(travelSaveDto);
        updatedTravel.setId(travel.getId());
        updatedTravel.setType(updatedType);
        updatedTravel.setCreator(user);
        long participantCount = 10L;
        Country country1 = CountryDataTest.getCountry1();
        Country country2 = CountryDataTest.getCountry1();
        Country country3 = CountryDataTest.getCountry2();
        City city1 = CityDataTest.getCity1();
        City city2 = CityDataTest.getCity2();
        Route route1 = RouteDataTest.getRoute1();
        Route route2 = RouteDataTest.getRoute2();
        Route route3 = RouteDataTest.getRoute3();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        when(typeRepository.findById(any())).thenReturn(Optional.of(updatedType));
        when(participantRepository.countByTravel(any())).thenReturn(participantCount);
        when(travelRepository.save(any())).thenReturn(updatedTravel);
        when(countryRepository.findById(any()))
                .thenReturn(Optional.of(country1))
                .thenReturn(Optional.of(country2))
                .thenReturn(Optional.of(country3));
        when(cityRepository.findById(any()))
                .thenReturn(Optional.of(city1))
                .thenReturn(Optional.of(city2))
                .thenReturn(Optional.empty());
        when(routeRepository.save(any()))
                .thenReturn(route1)
                .thenReturn(route2)
                .thenReturn(route3);
        TravelDto result = travelService.updateTravel(travel.getId(), travelSaveDto, auth);

        verify(routeRepository, times(1)).deleteAllByTravel(any());
        assertThat(result, allOf(
                hasProperty("id", equalTo(travel.getId())),
                hasProperty("title", equalTo(travelSaveDto.getTitle())),
                hasProperty("type", allOf(
                        hasProperty("id", equalTo(travelSaveDto.getTypeId())),
                        hasProperty("name", equalTo(travel.getType().getNameEn()))
                )),
                hasProperty("description", equalTo(travelSaveDto.getDescription())),
                hasProperty("limit", equalTo(travelSaveDto.getLimit())),
                hasProperty("startDate", equalTo(travelSaveDto.getStartDate())),
                hasProperty("creator", equalTo(userShortDto)),
                hasProperty("route", hasItems(
                        RouteDataTest.getRouteDto1(),
                        RouteDataTest.getRouteDto2(),
                        RouteDataTest.getRouteDto3()
                ))
        ));
    }

    @Test
    void updateTravelWithTravelNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel2();
        Authentication auth = mock(Authentication.class);
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();

        when(travelRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.updateTravel(travel.getId(), travelSaveDto, auth));
    }

    @Test
    void updateTravelWithForbiddenExceptionTest() {
        Travel travel = TravelDataTest.getTravel2();
        Authentication auth = mock(Authentication.class);
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        User user = UserDataTest.getUser2();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(user);

        assertThrows(ForbiddenException.class, () -> travelService.updateTravel(travel.getId(), travelSaveDto, auth));
    }

    @Test
    void updateTravelWithTypeNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel2();
        Authentication auth = mock(Authentication.class);
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        when(typeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.updateTravel(travel.getId(), travelSaveDto, auth));
    }

    @Test
    void updateTravelWithLimitBadRequestExceptionTest() {
        Travel travel = TravelDataTest.getTravel2();
        Authentication auth = mock(Authentication.class);
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Type updatedType = TypeDataTest.getType1();
        long participantCount = 15L;

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        when(typeRepository.findById(any())).thenReturn(Optional.of(updatedType));
        when(participantRepository.countByTravel(any())).thenReturn(participantCount);

        assertThrows(BadRequestException.class, () -> travelService.updateTravel(travel.getId(), travelSaveDto, auth));
    }

    @Test
    void updateTravelWithCountryNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel2();
        Authentication auth = mock(Authentication.class);
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Type updatedType = TypeDataTest.getType1();
        Travel updatedTravel = TravelMapper.INSTANCE.fromTravelSaveDto(travelSaveDto);
        updatedTravel.setId(travel.getId());
        updatedTravel.setType(updatedType);
        long participantCount = 10L;

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        when(typeRepository.findById(any())).thenReturn(Optional.of(updatedType));
        when(participantRepository.countByTravel(any())).thenReturn(participantCount);
        when(travelRepository.save(any())).thenReturn(updatedTravel);
        when(countryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.updateTravel(travel.getId(), travelSaveDto, auth));
    }

    @Test
    void updateTravelWithCityBadRequestExceptionTest() {
        Travel travel = TravelDataTest.getTravel2();
        Authentication auth = mock(Authentication.class);
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Type updatedType = TypeDataTest.getType1();
        Travel updatedTravel = TravelMapper.INSTANCE.fromTravelSaveDto(travelSaveDto);
        updatedTravel.setId(travel.getId());
        updatedTravel.setType(updatedType);
        long participantCount = 10L;
        Country country = CountryDataTest.getCountry2();
        City city = CityDataTest.getCity1();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        when(typeRepository.findById(any())).thenReturn(Optional.of(updatedType));
        when(participantRepository.countByTravel(any())).thenReturn(participantCount);
        when(travelRepository.save(any())).thenReturn(updatedTravel);
        when(countryRepository.findById(any())).thenReturn(Optional.of(country));
        when(cityRepository.findById(any())).thenReturn(Optional.of(city));

        assertThrows(BadRequestException.class, () -> travelService.updateTravel(travel.getId(), travelSaveDto, auth));
    }

    @Test
    void deleteTravelTest() {
        Travel travel = TravelDataTest.getTravel1();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(travel.getCreator());
        travelService.deleteTravel(travel.getId(), auth);

        verify(travelRepository, times(1)).delete(any());
    }

    @Test
    void deleteTravelWithNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        Authentication auth = mock(Authentication.class);

        when(travelRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.deleteTravel(travel.getId(), auth));
    }

    @Test
    void deleteTravelWithForbiddenExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser2();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(auth.getPrincipal()).thenReturn(user);

        assertThrows(ForbiddenException.class, () -> travelService.deleteTravel(travel.getId(), auth));
    }
}
