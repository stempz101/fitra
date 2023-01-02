package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.NotFoundException;
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
        when(countryRepository.findById(any()))
                .thenReturn(Optional.of(country1))
                .thenReturn(Optional.of(country2))
                .thenReturn(Optional.of(country3));
        when(cityRepository.findById(any()))
                .thenReturn(Optional.of(city1))
                .thenReturn(Optional.of(city2))
                .thenReturn(Optional.empty());
        when(travelRepository.save(any())).thenReturn(travel);
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
        List<Route> routes1 = List.of(route1, route2, route3);
        List<Route> routes2 = List.of(route3, route1, route2);
        List<Route> routes3 = List.of(route2, route3, route1);


        when(routeRepository.findAllByTravel(any(), any()))
                .thenReturn(routes1)
                .thenReturn(routes2)
                .thenReturn(routes3);
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
