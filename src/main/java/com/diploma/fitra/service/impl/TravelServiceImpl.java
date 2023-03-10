package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.*;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.*;
import com.diploma.fitra.model.*;
import com.diploma.fitra.model.enums.Role;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.model.key.ParticipantKey;
import com.diploma.fitra.repo.*;
import com.diploma.fitra.service.TravelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public TravelDto createTravel(TravelSaveDto travelSaveDto, UserDetails userDetails) {
        log.info("Creating travel: {}", travelSaveDto);

        User creator = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        Type type = typeRepository.findById(travelSaveDto.getTypeId())
                .orElseThrow(() -> new NotFoundException(Error.TYPE_NOT_FOUND.getMessage()));
        if (travelSaveDto.getEndDate().isBefore(travelSaveDto.getStartDate())
                || travelSaveDto.getEndDate().isEqual(travelSaveDto.getStartDate())) {
            throw new BadRequestException(Error.END_DATE_MUST_BE_AFTER_START_DATE.getMessage());
        } else if (travelSaveDto.getRoute().size() < 1) {
            throw new BadRequestException(Error.ROUTE_SIZE_MUST_BE_GREATER_THAN_ZERO.getMessage());
        } else if (travelSaveDto.getAgeFrom() > travelSaveDto.getAgeTo()) {
            throw new BadRequestException(Error.AGE_FROM_MUST_BE_LOWER_THAN_OR_EQUAL_TO_AGE_TO.getMessage());
        }

        Travel travel = TravelMapper.INSTANCE.fromTravelSaveDto(travelSaveDto);
        travel.setType(type);
        travel.setCreator(creator);
        travel.setCreatedTime(LocalDateTime.now());
        travel = travelRepository.save(travel);

        saveParticipant(creator, travel, creator.equals(travel.getCreator()));
        saveRouteList(travel, travelSaveDto.getRoute());
        saveEvents(travel, travelSaveDto);

        log.info("Travel is created: {}", travel);
        return toTravelDto(travel, true);
    }

    @Override
    public List<TravelDto> getTravels(Pageable pageable) {
        log.info("Getting travels");

        return travelRepository.findAllByBlockedIsFalseOrderByCreatedTimeDesc(pageable)
                .stream()
                .map(travel -> toTravelDto(travel, false))
                .collect(Collectors.toList());
    }

    @Override
    public List<TravelDto> getTravelsForUser(Pageable pageable, UserDetails userDetails) {
        log.info("Getting travels for user");

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));

        return travelRepository.findAllForUser(user, pageable)
                .stream()
                .map(travel -> toTravelDto(travel, false))
                .collect(Collectors.toList());
    }

    @Override
    public TravelDto getTravel(Long travelId, UserDetails userDetails) {
        log.info("Getting travel (id={})", travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));

        if (userDetails != null) {
            User user = (User) userDetails;
            boolean userExistsInTravel = participantRepository.existsByTravelIdAndUserId(travel.getId(), user.getId());
            if (userExistsInTravel) {
                return toTravelDto(travel, true);
            }
        }

        return toTravelDto(travel, false);
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
    public void removeUser(Long travelId, Long userId, UserDetails userDetails) {
        log.info("Removing user (id={}) from the travel with id: {}", userId, travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (user.getEmail().equals(travel.getCreator().getEmail())) {
            throw new BadRequestException(Error.CREATOR_CANT_REMOVE_HIMSELF.getMessage());
        }
        Participant participant = participantRepository.findById(new ParticipantKey(travel.getId(), user.getId()))
                .orElseThrow(() -> new ExistenceException(Error.USER_DOES_NOT_EXIST_IN_TRAVEL.getMessage()));

        participantRepository.delete(participant);
        log.info("User (id={}) is removed from the travel (id={})", userId, travelId);
    }

    @Override
    @Transactional
    public void leaveTravel(Long travelId, UserDetails userDetails) {
        log.info("Leaving from the travel (id={})", travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        Participant participant = participantRepository.findById(new ParticipantKey(travel.getId(), user.getId()))
                .orElseThrow(() -> new ExistenceException(Error.USER_DOES_NOT_EXIST_IN_TRAVEL.getMessage()));

        participantRepository.delete(participant);
        log.info("User (id={}) left the travel with id={} successfully", user.getId(), travel.getId());

        if (user.equals(travel.getCreator())) {
            travelRepository.delete(travel);
            log.info("Travel (id={}) is deleted due to the leaving from it by creator (id={})", travel.getId(), user.getId());
        }
    }

    @Override
    public EventDto createEvent(Long travelId, EventSaveDto eventSaveDto, UserDetails userDetails) {
        log.info("Creating event in the travel (id={})", travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getEmail().equals(userDetails.getUsername())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        } else if (eventSaveDto.getEndTime().isBefore(eventSaveDto.getStartTime())) {
            throw new BadRequestException(Error.END_TIME_MUST_BE_EQUAL_TO_OR_GREATER_THAN_START_TIME.getMessage());
        }

        Event event = saveEvent(travel, eventSaveDto);

        log.info("Event (id={}) in the travel (id={}) is created successfully", event.getId(), event.getTravel().getId());
        return EventMapper.INSTANCE.toEventDto(event);
    }

    @Override
    public EventDto updateEvent(Long travelId, Long eventId, EventSaveDto eventSaveDto, UserDetails userDetails) {
        log.info("Updating event (id={}) in the travel (id={})", eventId, travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getEmail().equals(userDetails.getUsername())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        } else if (eventSaveDto.getEndTime().isBefore(eventSaveDto.getStartTime())) {
            throw new BadRequestException(Error.END_TIME_MUST_BE_EQUAL_TO_OR_GREATER_THAN_START_TIME.getMessage());
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Error.EVENT_NOT_FOUND.getMessage()));

        event = UpdateMapper.updateEventWithPresentEventSaveDtoFields(event, eventSaveDto);
        event = eventRepository.save(event);

        log.info("Event (id={}) in the travel (id={}) is updated successfully!", event.getId(), event.getTravel().getId());
        return EventMapper.INSTANCE.toEventDto(event);
    }

    @Override
    public void deleteEvent(Long travelId, Long eventId, UserDetails userDetails) {
        log.info("Deleting event (id={}) in the travel (id={})", eventId, travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getEmail().equals(userDetails.getUsername())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Error.EVENT_NOT_FOUND.getMessage()));

        eventRepository.delete(event);
        log.info("Event (id={}) in the travel (id={}) is deleted successfully!", event.getId(), event.getTravel().getId());
    }

    @Override
    public TravelDto updateTravel(Long travelId, TravelSaveDto travelSaveDto, UserDetails userDetails) {
        log.info("Updating travel (id={})", travelId);

        Travel oldTravel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!oldTravel.getCreator().getEmail().equals(userDetails.getUsername())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        }

        Type type = typeRepository.findById(travelSaveDto.getTypeId())
                .orElseThrow(() -> new NotFoundException(Error.TYPE_NOT_FOUND.getMessage()));
        long participantCount = participantRepository.countByTravelId(oldTravel.getId());
        if (travelSaveDto.getEndDate().isBefore(travelSaveDto.getStartDate())
                || travelSaveDto.getEndDate().isEqual(travelSaveDto.getStartDate())) {
            throw new BadRequestException(Error.END_DATE_MUST_BE_AFTER_START_DATE.getMessage());
        } else if (travelSaveDto.getLimit() < participantCount) {
            throw new BadRequestException(Error.LIMIT_IS_LOWER_THAN_CURRENT_COUNT.getMessage());
        } else if (travelSaveDto.getAgeFrom() > travelSaveDto.getAgeTo()) {
            throw new BadRequestException(Error.AGE_FROM_MUST_BE_LOWER_THAN_OR_EQUAL_TO_AGE_TO.getMessage());
        }

        Travel travel = UpdateMapper.updateTravelWithPresentTravelSaveDtoFields(oldTravel, travelSaveDto);
        travel.setType(type);
        travel = travelRepository.save(travel);

        log.info("Travel (id={}) is updated: {}", travel.getId(), travel);
        return toTravelDto(travel, true);
    }

    @Override
    @Transactional
    public List<RouteDto> updateRoute(Long travelId, RouteSaveDto routeSaveDto, UserDetails userDetails) {

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getEmail().equals(userDetails.getUsername())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        } else if (routeSaveDto.getRoute().size() < 1) {
            throw new BadRequestException(Error.ROUTE_SIZE_MUST_BE_GREATER_THAN_ZERO.getMessage());
        }

        List<Route> routeList = checkAndMapRouteDtoList(routeSaveDto.getRoute());
        routeRepository.deleteAllByTravelId(travel.getId());
        Route route;
        for (int i = 0; i < routeList.size(); i++) {
            route = routeList.get(i);
            route.setTravel(travel);
            route = routeRepository.save(route);
            routeList.set(i, route);
        }

        log.info("Route for the travel (id={}) is updated: {}", travel.getId(), routeList);
        return routeList.stream()
                .map(RouteMapper.INSTANCE::toRouteDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTravel(Long travelId, UserDetails userDetails) {
        log.info("Deleting travel (id={})", travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getEmail().equals(userDetails.getUsername())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        }

        travelRepository.delete(travel);
        log.info("Travel (id={}) is deleted", travelId);
    }

    private void saveRouteList(Travel travel, List<RouteDto> routeDtoList) {
        List<Route> routeList = checkAndMapRouteDtoList(routeDtoList);
        for (Route route : routeList) {
            route.setTravel(travel);
            routeRepository.save(route);
        }
        log.info("Route for the travel (id={}) is built: {}", travel.getId(), routeList);
    }

    private void saveEvents(Travel travel, TravelSaveDto travelSaveDto) {
        if (travelSaveDto.getEvents() != null && !travelSaveDto.getEvents().isEmpty()) {
            for (EventSaveDto eventSaveDto : travelSaveDto.getEvents()) {
                if (eventSaveDto.getEndTime().isBefore(eventSaveDto.getStartTime())) {
                    throw new BadRequestException(Error.END_TIME_MUST_BE_EQUAL_TO_OR_GREATER_THAN_START_TIME.getMessage());
                }
                saveEvent(travel, eventSaveDto);
            }
        }
    }

    private Event saveEvent(Travel travel, EventSaveDto eventSaveDto) {
        Event event = EventMapper.INSTANCE.fromEventSaveDto(eventSaveDto);
        event.setTravel(travel);
        return eventRepository.save(event);
    }

    private List<RouteDto> getRouteDtoList(Travel travel) {
        return routeRepository.findAllByTravelIdOrderByPositionAsc(travel.getId())
                .stream()
                .map(RouteMapper.INSTANCE::toRouteDto)
                .collect(Collectors.toList());
    }

    private List<EventDto> getEventDtoList(Travel travel) {
        return eventRepository.findAllByTravelIdOrderByStartTimeAsc(travel.getId())
                .stream()
                .map(EventMapper.INSTANCE::toEventDto)
                .collect(Collectors.toList());
    }

    private void saveParticipant(User user, Travel travel, boolean isCreator) {
        Participant participant = new Participant();
        participant.setTravel(travel);
        participant.setUser(user);
        participant.setCreator(isCreator);
        participantRepository.save(participant);

        log.info("New participant (userId={}, isCreator={}) is inserted to the travel (id={})",
                user.getId(), isCreator, travel.getId());
    }

    private List<Route> checkAndMapRouteDtoList(List<RouteDto> routeDtoList) {
        List<Route> routeList = new ArrayList<>();
        Route route;
        Country country;
        City city = null;
        for (RouteDto routeDto : routeDtoList) {
            country = countryRepository.findById(routeDto.getCountryId())
                    .orElseThrow(() -> new NotFoundException(Error.COUNTRY_NOT_FOUND.getMessage()));
            if (routeDto.getCityId() != null) {
                city = cityRepository.findById(routeDto.getCityId())
                        .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND.getMessage()));
                if (!city.getCountry().getId().equals(country.getId())) {
                    throw new BadRequestException(Error.CITY_NOT_IN_COUNTRY.getMessage());
                }
            }
            route = new Route();
            route.setCountry(country);
            route.setCity(city);
            route.setPosition(routeDto.getPosition());
            routeList.add(route);
        }
        return routeList;
    }

    private TravelDto toTravelDto(Travel travel, boolean showEvents) {
        TravelDto travelDto = TravelMapper.INSTANCE.toTravelDto(travel);
        travelDto.setType(TypeMapper.INSTANCE.toTypeDto(travel.getType()));
        travelDto.setCreator(UserMapper.INSTANCE.toUserShortDto(travel.getCreator()));
        if (travel.getCreator().getRole().equals(Role.ADMIN)) {
            travelDto.getCreator().setIsAdmin(true);
        }
        travelDto.setRoute(getRouteDtoList(travel));
        if (showEvents) {
            travelDto.setEvents(getEventDtoList(travel));
        }
        return travelDto;
    }

    private ParticipantDto toParticipantDto(Participant participant) {
        ParticipantDto participantDto = ParticipantMapper.INSTANCE.toParticipantDto(participant);
        if (participant.isCreator()) {
            participantDto.setIsCreator(true);
        }
        if (participant.getUser().getRole().equals(Role.ADMIN)) {
            participantDto.getUser().setIsAdmin(true);
        }
        return participantDto;
    }
}
