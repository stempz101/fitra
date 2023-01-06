package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import com.diploma.fitra.dto.user.UserDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
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
        Country country1 = CountryDataTest.getCountry1();
        Country country2 = CountryDataTest.getCountry1();
        Country country3 = CountryDataTest.getCountry2();
        City city1 = CityDataTest.getCity1();
        City city2 = CityDataTest.getCity2();
        Route route1 = RouteDataTest.getRoute1();
        Route route2 = RouteDataTest.getRoute2();
        Route route3 = RouteDataTest.getRoute3();
        Participant participant = ParticipantDataTest.getParticipant1();

        when(typeRepository.findById(any())).thenReturn(Optional.of(travel.getType()));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
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
        TravelDto result = travelService.createTravel(travelSaveDto);

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
                hasProperty("route", hasItems(
                        RouteDataTest.getRouteDto1(),
                        RouteDataTest.getRouteDto2(),
                        RouteDataTest.getRouteDto3()
                ))
        ));
    }

    @Test
    void createTravelWithTypeNotFoundExceptionTest() {
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();

        when(typeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.createTravel(travelSaveDto));
    }

    @Test
    void createTravelWithUserNotFoundExceptionTest() {
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Travel travel = TravelDataTest.getTravel1();

        when(typeRepository.findById(any())).thenReturn(Optional.of(travel.getType()));
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.createTravel(travelSaveDto));
    }

    @Test
    void createTravelWithCountryNotFoundExceptionTest() {
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser1();

        when(typeRepository.findById(any())).thenReturn(Optional.of(travel.getType()));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(countryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.createTravel(travelSaveDto));
    }

    @Test
    void createTravelWithCityBadRequestExceptionTest() {
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser1();
        Country country = CountryDataTest.getCountry2();
        City city = CityDataTest.getCity1();

        when(typeRepository.findById(any())).thenReturn(Optional.of(travel.getType()));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(countryRepository.findById(any())).thenReturn(Optional.of(country));
        when(cityRepository.findById(any())).thenReturn(Optional.of(city));

        assertThrows(BadRequestException.class, () -> travelService.createTravel(travelSaveDto));
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

        assertThat(result, hasSize(travels.size()));
        assertThat(result, hasItems(
                TravelDataTest.getTravelDto1(),
                TravelDataTest.getTravelDto2(),
                TravelDataTest.getTravelDto3()
        ));
    }

    @Test
    void addUserTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        UserDto userDto = UserDataTest.getUserDto2();
        Participant participant = ParticipantDataTest.getParticipant2();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(participantRepository.findById(any())).thenReturn(Optional.empty());
        when(participantRepository.save(any())).thenReturn(participant);
        ParticipantDto result = travelService.addUser(travel.getId(), user.getId());

        assertThat(result, allOf(
                hasProperty("travelId", equalTo(travel.getId())),
                hasProperty("user", equalTo(userDto)),
                hasProperty("isCreator", equalTo(false))
        ));
    }

    @Test
    void addUserWithTravelNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();

        when(travelRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.addUser(travel.getId(), user.getId()));
    }

    @Test
    void addUserWithUserNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.addUser(travel.getId(), user.getId()));
    }

    @Test
    void addUserWithUserIsAdminBadRequestExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser1();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> travelService.addUser(travel.getId(), user.getId()));
    }

    @Test
    void addUserWithExistenceExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();
        Participant participant = ParticipantDataTest.getParticipant2();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(participantRepository.findById(any())).thenReturn(Optional.of(participant));

        assertThrows(ExistenceException.class, () -> travelService.addUser(travel.getId(), user.getId()));
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

        ParticipantDto participantDto1 = ParticipantDataTest.getParticipantDto1();
        ParticipantDto participantDto2 = ParticipantDataTest.getParticipantDto2();
        ParticipantDto participantDto3 = ParticipantDataTest.getParticipantDto3();
        System.out.println(participantDto1.equals(result.get(0)));
        System.out.println(participantDto2.equals(result.get(1)));
        System.out.println(participantDto3.equals(result.get(2)));

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
        User user = UserDataTest.getUser2();
        Participant participant = ParticipantDataTest.getParticipant2();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(participantRepository.findById(any())).thenReturn(Optional.of(participant));
        travelService.removeUser(travel.getId(), user.getId());

        verify(participantRepository, times(1)).delete(any());
    }

    @Test
    void removeUserWithTravelNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();

        when(travelRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.removeUser(travel.getId(), user.getId()));
    }

    @Test
    void removeUserWithUserNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.removeUser(travel.getId(), user.getId()));
    }

    @Test
    void removeUserWithExistenceExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();
        User user = UserDataTest.getUser2();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(participantRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ExistenceException.class, () -> travelService.removeUser(travel.getId(), user.getId()));
    }

    @Test
    void updateTravelTest() {
        Travel travel = TravelDataTest.getTravel2();
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Type updatedType = TypeDataTest.getType1();
        Travel updatedTravel = TravelMapper.INSTANCE.fromTravelSaveDto(travelSaveDto);
        updatedTravel.setId(travel.getId());
        updatedTravel.setType(updatedType);
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
        TravelDto result = travelService.updateTravel(travel.getId(), travelSaveDto);

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
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();

        when(travelRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.updateTravel(travel.getId(), travelSaveDto));
    }

    @Test
    void updateTravelWithTypeNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel2();
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(typeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.updateTravel(travel.getId(), travelSaveDto));
    }

    @Test
    void updateTravelWithLimitBadRequestExceptionTest() {
        Travel travel = TravelDataTest.getTravel2();
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Type updatedType = TypeDataTest.getType1();
        long participantCount = 15L;

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(typeRepository.findById(any())).thenReturn(Optional.of(updatedType));
        when(participantRepository.countByTravel(any())).thenReturn(participantCount);

        assertThrows(BadRequestException.class, () -> travelService.updateTravel(travel.getId(), travelSaveDto));
    }

    @Test
    void updateTravelWithCountryNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel2();
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Type updatedType = TypeDataTest.getType1();
        Travel updatedTravel = TravelMapper.INSTANCE.fromTravelSaveDto(travelSaveDto);
        updatedTravel.setId(travel.getId());
        updatedTravel.setType(updatedType);
        long participantCount = 10L;

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(typeRepository.findById(any())).thenReturn(Optional.of(updatedType));
        when(participantRepository.countByTravel(any())).thenReturn(participantCount);
        when(travelRepository.save(any())).thenReturn(updatedTravel);
        when(countryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.updateTravel(travel.getId(), travelSaveDto));
    }

    @Test
    void updateTravelWithCityBadRequestExceptionTest() {
        Travel travel = TravelDataTest.getTravel2();
        TravelSaveDto travelSaveDto = TravelDataTest.getTravelSaveDto1();
        Type updatedType = TypeDataTest.getType1();
        Travel updatedTravel = TravelMapper.INSTANCE.fromTravelSaveDto(travelSaveDto);
        updatedTravel.setId(travel.getId());
        updatedTravel.setType(updatedType);
        long participantCount = 10L;
        Country country = CountryDataTest.getCountry2();
        City city = CityDataTest.getCity1();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        when(typeRepository.findById(any())).thenReturn(Optional.of(updatedType));
        when(participantRepository.countByTravel(any())).thenReturn(participantCount);
        when(travelRepository.save(any())).thenReturn(updatedTravel);
        when(countryRepository.findById(any())).thenReturn(Optional.of(country));
        when(cityRepository.findById(any())).thenReturn(Optional.of(city));

        assertThrows(BadRequestException.class, () -> travelService.updateTravel(travel.getId(), travelSaveDto));
    }

    @Test
    void deleteTravelTest() {
        Travel travel = TravelDataTest.getTravel1();

        when(travelRepository.findById(any())).thenReturn(Optional.of(travel));
        travelService.deleteTravel(travel.getId());

        verify(travelRepository, times(1)).delete(any());
    }

    @Test
    void deleteTravelWithNotFoundExceptionTest() {
        Travel travel = TravelDataTest.getTravel1();

        when(travelRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> travelService.deleteTravel(travel.getId()));
    }
}
