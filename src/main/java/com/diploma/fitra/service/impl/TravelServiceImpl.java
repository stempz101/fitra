package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.travel.RouteDto;
import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.RouteMapper;
import com.diploma.fitra.mapper.TravelMapper;
import com.diploma.fitra.mapper.TypeMapper;
import com.diploma.fitra.model.*;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.*;
import com.diploma.fitra.service.TravelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final UserRepository userRepository;
    private final TypeRepository typeRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final RouteRepository routeRepository;
    private final ParticipantRepository participantRepository;

    @Override
    @Transactional
    public TravelDto createTravel(TravelSaveDto travelSaveDto) {
        log.info("Saving type: {}", travelSaveDto);

        Type type = typeRepository.findById(travelSaveDto.getTypeId())
                .orElseThrow(() -> new NotFoundException(Error.TYPE_NOT_FOUND.getMessage()));
        User creator = userRepository.findById(travelSaveDto.getCreatorId())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        List<Route> routeList = checkAndMapRouteDtoList(travelSaveDto.getRoute());

        Travel travel = TravelMapper.INSTANCE.fromTravelSaveDto(travelSaveDto);
        travel.setType(type);
        travel = travelRepository.save(travel);
        log.info("Travel is created: {}", travel);

        for (Route route : routeList) {
            route.setTravel(travel);
            routeRepository.save(route);
        }
        log.info("Route for the travel (id={}) is built: {}", travel.getId(), routeList);

        Participant participant = new Participant();
        participant.setTravel(travel);
        participant.setUser(creator);
        participant.setIsCreator(true);
        participant = participantRepository.save(participant);
        log.info("Creator is became a participant of created travel: {}", participant);

        TravelDto travelDto = TravelMapper.INSTANCE.toTravelDto(travel);
        travelDto.setType(TypeMapper.INSTANCE.toTypeDto(travel.getType()));
        List<RouteDto> routeDtoList = routeList.stream()
                .map(RouteMapper.INSTANCE::toRouteDto)
                .collect(Collectors.toList());
        travelDto.setRoute(routeDtoList);
        return travelDto;
    }

    @Override
    public List<TravelDto> getTravels() {
        log.info("Getting travels");

        return travelRepository.findAll().stream()
                .map(travel -> {
                    TravelDto travelDto = TravelMapper.INSTANCE.toTravelDto(travel);
                    travelDto.setType(TypeMapper.INSTANCE.toTypeDto(travel.getType()));
                    List<RouteDto> routeDtoList = routeRepository.findAllByTravel(travel, Sort.by("priority"))
                            .stream()
                            .map(RouteMapper.INSTANCE::toRouteDto)
                            .collect(Collectors.toList());
                    travelDto.setRoute(routeDtoList);
                    return travelDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTravel(Long travelId) {
        log.info("Deleting travel with id: {}", travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));

        travelRepository.delete(travel);
        log.info("Travel (id={}) is deleted", travelId);
    }

    private List<Route> checkAndMapRouteDtoList(List<RouteDto> routeDtoList) {
        List<Route> routeList = new ArrayList<>();
        Route route;
        Country country;
        City city;
        int priority = 0;
        for (RouteDto routeDto : routeDtoList) {
            country = countryRepository.findById(routeDto.getCountryId())
                    .orElseThrow(() -> new NotFoundException(Error.COUNTRY_NOT_FOUND.getMessage()));
            city = cityRepository.findById(routeDto.getCityId()).orElse(null);
            if (city != null && !city.getCountry().getId().equals(country.getId())) {
                throw new BadRequestException(Error.CITY_NOT_IN_COUNTRY.getMessage());
            }
            route = new Route();
            route.setCountry(country);
            route.setCity(city);
            route.setPriority(++priority);
            routeList.add(route);
        }
        return routeList;
    }
}
