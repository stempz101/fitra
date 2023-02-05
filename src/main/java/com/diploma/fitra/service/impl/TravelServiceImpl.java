package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.RouteDto;
import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.*;
import com.diploma.fitra.model.*;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.model.key.ParticipantKey;
import com.diploma.fitra.model.role.Role;
import com.diploma.fitra.repo.*;
import com.diploma.fitra.service.TravelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
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
    private final InvitationRepository invitationRepository;

    @Override
    @Transactional
    public TravelDto createTravel(TravelSaveDto travelSaveDto) {
        log.info("Saving type: {}", travelSaveDto);

        Type type = typeRepository.findById(travelSaveDto.getTypeId())
                .orElseThrow(() -> new NotFoundException(Error.TYPE_NOT_FOUND.getMessage()));
        User creator = userRepository.findById(travelSaveDto.getCreatorId())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));

        Travel travel = TravelMapper.INSTANCE.fromTravelSaveDto(travelSaveDto);
        travel.setType(type);
        travel = travelRepository.save(travel);
        log.info("Travel is created: {}", travel);

        List<Route> routeList = checkAndMapRouteDtoList(travelSaveDto.getRoute());
        for (Route route : routeList) {
            route.setTravel(travel);
            routeRepository.save(route);
        }
        log.info("Route for the travel (id={}) is built: {}", travel.getId(), routeList);

        Participant participant = new Participant();
        participant.setTravel(travel);
        participant.setUser(creator);
        participant.setCreator(true);
        participant = participantRepository.save(participant);
        log.info("Creator is became a participant of created travel: {}", participant);

        return toTravelDto(travel, routeList);
    }

    @Override
    public List<TravelDto> getTravels() {
        log.info("Getting travels");

        return travelRepository.findAll().stream()
                .map(travel -> {
                    TravelDto travelDto = TravelMapper.INSTANCE.toTravelDto(travel);
                    travelDto.setType(TypeMapper.INSTANCE.toTypeDto(travel.getType()));
                    List<RouteDto> routeDtoList = routeRepository.findAllByTravel(travel, Sort.by("position"))
                            .stream()
                            .map(RouteMapper.INSTANCE::toRouteDto)
                            .collect(Collectors.toList());
                    travelDto.setRoute(routeDtoList);
                    return travelDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void addUser(Long travelId, Long userId) {
        log.info("Adding user (id={}) to a travel with id: {}", userId, travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (user.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException(Error.ADMIN_CANT_BE_ADDED_TO_TRAVEL.getMessage());
        } else if (participantRepository.findById(new ParticipantKey(travel.getId(), user.getId())).isPresent()) {
            throw new ExistenceException(Error.USER_EXISTS_IN_TRAVEL.getMessage());
        }

        Invitation invitation = new Invitation();
        invitation.setTravel(travel);
        invitation.setUser(user);
        invitationRepository.save(invitation);

        log.info("Travel (id={}) invitation is created to the user (id={})", travelId, userId);
    }

    @Override
    public List<ParticipantDto> getUsers(Long travelId) {
        log.info("Getting users from travel (id={})", travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));

        return participantRepository.findAllByTravel(travel).stream()
                .map(this::toParticipantDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeUser(Long travelId, Long userId) {
        log.info("Removing user (id={}) from the travel with id: {}", userId, travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        Participant participant = participantRepository.findById(new ParticipantKey(travel.getId(), user.getId()))
                .orElseThrow(() -> new ExistenceException(Error.USER_DOES_NOT_EXIST_IN_TRAVEL.getMessage()));

        participantRepository.delete(participant);
        log.info("User (id={}) is removed from the travel (id={})", userId, travelId);
    }

    @Override
    @Transactional
    public TravelDto updateTravel(Long travelId, TravelSaveDto travelSaveDto) {
        log.info("Updating travel (id={}): {}", travelId, travelSaveDto);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        Type type = typeRepository.findById(travelSaveDto.getTypeId())
                .orElseThrow(() -> new NotFoundException(Error.TYPE_NOT_FOUND.getMessage()));
        long participantCount = participantRepository.countByTravel(travel);
        if (travelSaveDto.getLimit() < participantCount) {
            throw new BadRequestException(Error.LIMIT_IS_LOWER_THAN_CURRENT_COUNT.getMessage());
        }

        travel = UpdateMapper.updateTravelWithPresentTravelSaveDtoFields(travel, travelSaveDto);
        travel.setType(type);
        travel = travelRepository.save(travel);
        log.info("Travel is updated: {}", travel);

        List<Route> routeList = checkAndMapRouteDtoList(travelSaveDto.getRoute());
        routeRepository.deleteAllByTravel(travel);
        for (Route route : routeList) {
            route.setTravel(travel);
            routeRepository.save(route);
        }
        log.info("Route for the travel (id={}) is updated: {}", travel.getId(), routeList);

        return toTravelDto(travel, routeList);
    }

    @Override
    @Transactional
    public void deleteTravel(Long travelId) {
        log.info("Deleting travel with id: {}", travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));

        travelRepository.delete(travel);
        log.info("Travel (id={}) is deleted", travelId);
    }

    private TravelDto toTravelDto(Travel travel, List<Route> routeList) {
        TravelDto travelDto = TravelMapper.INSTANCE.toTravelDto(travel);
        travelDto.setType(TypeMapper.INSTANCE.toTypeDto(travel.getType()));
        List<RouteDto> routeDtoList = routeList.stream()
                .map(RouteMapper.INSTANCE::toRouteDto)
                .sorted(Comparator.comparingInt(RouteDto::getPosition))
                .collect(Collectors.toList());
        travelDto.setRoute(routeDtoList);
        return travelDto;
    }

    private ParticipantDto toParticipantDto(Participant participant) {
        ParticipantDto participantDto = ParticipantMapper.INSTANCE.toParticipantDto(participant);
        if (participant.getUser().getRole().equals(Role.ADMIN)) {
            participantDto.getUser().setIsAdmin(true);
        }
        return participantDto;
    }

    private List<Route> checkAndMapRouteDtoList(List<RouteDto> routeDtoList) {
        List<Route> routeList = new ArrayList<>();
        Route route;
        Country country;
        City city;
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
            route.setPosition(routeDto.getPosition());
            routeList.add(route);
        }
        return routeList;
    }
}
