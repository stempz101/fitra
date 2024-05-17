package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.travel.*;
import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.user.UserDto;
import com.diploma.fitra.dto.user.UserShortDto;
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
import com.diploma.fitra.repo.custom.TravelCustomRepository;
import com.diploma.fitra.service.TravelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TravelServiceImpl implements TravelService {
    private final TravelRepository travelRepository;
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    private final TypeRepository typeRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final RouteRepository routeRepository;
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    private final TravelImageRepository travelImageRepository;

    private final TravelCustomRepository travelCustomRepository;

    private final ObjectMapper objectMapper;

    @Value("${photo-storage.travel-photos}")
    private String travelPhotoStoragePath;

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
        } else if (travelSaveDto.getAgeFrom() > travelSaveDto.getAgeTo()) {
            throw new BadRequestException(Error.AGE_FROM_MUST_BE_LOWER_THAN_OR_EQUAL_TO_AGE_TO.getMessage());
        }

        Travel travel = TravelMapper.INSTANCE.fromTravelSaveDto(travelSaveDto);
        travel.setType(type);
        travel.setCreator(creator);
        travel.setCreatedTime(LocalDateTime.now());
        travel = travelRepository.save(travel);

        saveParticipant(creator, travel, creator.equals(travel.getCreator()));
        saveTravelPhoto(travel, travelSaveDto.getPhoto(), true);

        List<RouteDto> routeDto = mapJsonToRouteDtoList(travelSaveDto.getRoute());
        saveRouteList(travel, routeDto);

        List<EventDto> eventDtoList = mapJsonToEventDtoList(travelSaveDto.getEvents());
        saveEvents(travel, eventDtoList);

        log.info("Travel is created: {}", travel);
        return toTravelDto(travel, true);
    }

    @Override
    public TravelItemsResponse getTravels(String name, Long countryId, Long cityId, Long typeId,
                                      LocalDate startDate, Integer peopleFrom, Integer peopleTo, Pageable pageable) {
        log.info("Getting travels");

        List<TravelDto> travels = travelCustomRepository.findAllByQueryParams(
                        name, countryId, cityId, typeId, startDate, peopleFrom, peopleTo, pageable
                )
                .stream()
                .map(travel -> toTravelDto(travel, false))
                .collect(Collectors.toList());
        long count = travelCustomRepository.countByQueryParams(name, countryId, cityId, typeId, startDate, peopleFrom, peopleTo);

        return TravelItemsResponse.builder()
                .items(travels)
                .totalItems(count)
                .build();
    }

    @Override
    public TravelItemsResponse getParticipatingTravels(Pageable pageable, UserDetails userDetails) {
        User user = (User) userDetails;

        log.info("Getting participating travels for user (id={})", user.getId());

        List<TravelDto> travels = travelRepository.findAllForUserOrderByStartDate(user, pageable)
                .stream()
                .map(travel -> toTravelDto(travel, false))
                .collect(Collectors.toList());
        long count = travelRepository.countAllForUser(user);

        return TravelItemsResponse.builder()
                .items(travels)
                .totalItems(count)
                .build();
    }

    @Override
    public TravelItemsResponse getCreatedTravels(Pageable pageable, UserDetails userDetails) {
        User user = (User) userDetails;

        log.info("Getting created travels for user (id={})", user.getId());

        List<TravelDto> travels = travelRepository.findAllByCreatorIdOrderByStartDate(user.getId(), pageable)
                .stream()
                .map(travel -> toTravelDto(travel, false))
                .collect(Collectors.toList());
        long count = travelRepository.countAllByCreatorId(user.getId());

        return TravelItemsResponse.builder()
                .items(travels)
                .totalItems(count)
                .build();
    }

    @Override
    public TravelDto getTravel(Long travelId, UserDetails userDetails) {
        log.info("Getting travel (id={})", travelId);

        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));

        return toTravelDto(travel, true);
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
    public List<UserDto> getUsersToInvite(Long travelId, String search, Pageable pageable) {
        log.info("Getting users to send travel (id={}) invitation", travelId);

        return userRepository.findAllNotInTravel(travelId, search, pageable)
                .stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    userDto.setId(user.getId());
                    userDto.setName(user.getFullName());
                    userDto.setIsAdmin(user.getRole().equals(Role.ADMIN));
                    return userDto;
                })
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

        User user = (User) userDetails;
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        Participant participant = participantRepository.findById(new ParticipantKey(travel.getId(), user.getId()))
                .orElseThrow(() -> new ExistenceException(Error.USER_DOES_NOT_EXIST_IN_TRAVEL.getMessage()));

        participantRepository.delete(participant);
        log.info("User (id={}) left the travel with id={} successfully", user.getId(), travel.getId());

        if (user.getId().equals(travel.getCreator().getId())) {
            List<TravelImage> images = travelImageRepository.findAllByTravelIdOrderById(travel.getId());
            deleteTravelImages(images);
            travelRepository.delete(travel);
            log.info("Travel (id={}) is deleted due to the leaving from it by creator (id={})", travel.getId(), user.getId());
        }
    }

    @Override
    public List<EventDto> getEvents(Long travelId) {
        log.info("Getting event of the travel (id={})", travelId);

        return eventRepository.findAllByTravelIdOrderByStartTimeAsc(travelId)
                .stream()
                .map(EventMapper.INSTANCE::toEventDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EventDto> setEvents(Long travelId, String events, UserDetails userDetails) {
        log.info("Setting events to the travel (id={})", travelId);

        User user = (User) userDetails;
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getId().equals(user.getId())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        }

        eventRepository.deleteAllByTravelId(travelId);
        List<Event> newEvents = mapJsonToEventDtoList(events).stream()
                .map(eventDto -> {
                    Event event = EventMapper.INSTANCE.fromEventDto(eventDto);
                    event.setTravel(travel);
                    return event;
                })
                .collect(Collectors.toList());
        return eventRepository.saveAll(newEvents)
                .stream()
                .map(EventMapper.INSTANCE::toEventDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TravelDto updateTravel(Long travelId, TravelSaveDto travelSaveDto, UserDetails userDetails) {
        log.info("Updating travel (id={})", travelId);

        User user = (User) userDetails;
        Travel oldTravel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!oldTravel.getCreator().getId().equals(user.getId())) {
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

        List<RouteDto> routeDto = mapJsonToRouteDtoList(travelSaveDto.getRoute());
        routeRepository.deleteAllByTravelId(travel.getId());
        saveRouteList(travel, routeDto);

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

        List<TravelImage> images = travelImageRepository.findAllByTravelIdOrderById(travel.getId());
        deleteTravelImages(images);
        travelRepository.delete(travel);
        log.info("Travel (id={}) is deleted", travelId);
    }

    private void deleteTravelImages(List<TravelImage> images) {
        for (TravelImage image : images) {
            File file = new File(travelPhotoStoragePath + image.getFileName());

            if (file.delete()) {
                log.info("Travel image is deleted successfully: {}", image.getFileName());
            } else {
                throw new RuntimeException(Error.TRAVEL_IMAGE_IS_NOT_DELETED.getMessage() + ": " + image.getFileName());
            }
        }
    }

    private List<RouteDto> mapJsonToRouteDtoList(String routeDtoJson) {
        try {
            return Arrays.asList(objectMapper.readValue(routeDtoJson, RouteDto[].class));
        } catch (JsonProcessingException e) {
            throw new BadRequestException(Error.ROUTE_MAPPING_ERROR.getMessage());
        }
    }

    private List<EventDto> mapJsonToEventDtoList(String eventDtoListJson) {
        try {
            return Arrays.asList(objectMapper.readValue(eventDtoListJson, EventDto[].class));
        } catch (JsonProcessingException e) {
            throw new BadRequestException(Error.EVENTS_MAPPING_ERROR.getMessage());
        }
    }

    private void saveRouteList(Travel travel, List<RouteDto> routeDtoList) {
        List<Route> routeList = checkAndMapRouteDtoList(routeDtoList);
        for (Route route : routeList) {
            route.setTravel(travel);
            routeRepository.save(route);
        }
        log.info("Route for the travel (id={}) is built: {}", travel.getId(), routeList);
    }

    private void saveEvents(Travel travel, List<EventDto> events) {
        if (events != null && !events.isEmpty()) {
            for (EventDto eventDto : events) {
                if (eventDto.getEndTime().isBefore(eventDto.getStartTime())) {
                    throw new BadRequestException(Error.END_TIME_MUST_BE_EQUAL_TO_OR_GREATER_THAN_START_TIME.getMessage());
                }
                saveEvent(travel, eventDto);
            }
        }
    }

    private Event saveEvent(Travel travel, EventDto eventDto) {
        Event event = EventMapper.INSTANCE.fromEventDto(eventDto);
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
        City city;
        for (RouteDto routeDto : routeDtoList) {
            route = new Route();

            country = countryRepository.findById(routeDto.getCountryId())
                    .orElseThrow(() -> new NotFoundException(Error.COUNTRY_NOT_FOUND.getMessage()));
            route.setCountry(country);

            if (routeDto.getCityId() != null) {
                city = cityRepository.findById(routeDto.getCityId())
                        .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND.getMessage()));
                if (!city.getCountry().getId().equals(country.getId())) {
                    throw new BadRequestException(Error.CITY_NOT_IN_COUNTRY.getMessage());
                }
                route.setCity(city);
            }

            route.setPosition(routeDto.getPosition());
            routeList.add(route);
        }
        return routeList;
    }

    private TravelDto toTravelDto(Travel travel, boolean showEvents) {
        TravelDto travelDto = TravelMapper.INSTANCE.toTravelDto(travel);
        travelDto.setType(toTypeDto(travel.getType()));
        travelDto.setCreator(toUserShortDto(travel.getCreator()));
        if (travel.getCreator().getRole().equals(Role.ADMIN)) {
            travelDto.getCreator().setIsAdmin(true);
        }
        travelDto.setRoute(getRouteDtoList(travel));
        if (showEvents) {
            travelDto.setEvents(getEventDtoList(travel));
        }
        return travelDto;
    }

    private UserShortDto toUserShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setFirstName(user.getFirstName());
        userShortDto.setName(user.getFullName());
        userShortDto.setBirthday(user.getBirthday());
        userShortDto.setCountry(CountryMapper.INSTANCE.toCountryDto(user.getCountry()));
        userShortDto.setCityDto(CityMapper.INSTANCE.toCityDto(user.getCity()));
        userShortDto.setAbout(user.getAbout());
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            userShortDto.setIsAdmin(true);
        }
        return userShortDto;
    }

    private TypeDto toTypeDto(Type type) {
        TypeDto typeDto = TypeMapper.INSTANCE.toTypeDto(type);
        typeDto.setName(type.getNameEn());
        return typeDto;
    }

    private ParticipantDto toParticipantDto(Participant participant) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(participant.getUser().getId());
        userShortDto.setName(participant.getUser().getFullName());
        userShortDto.setFirstName(participant.getUser().getFirstName());
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setUser(userShortDto);
        participantDto.setIsCreator(participant.isCreator());
        return participantDto;
    }

    private void saveTravelPhoto(Travel travel, MultipartFile photo, boolean isMain) {
        if (photo == null || photo.isEmpty()) {
            throw new BadRequestException(Error.PHOTO_IS_NULL_OR_EMPTY.getMessage());
        }

        String fileName = saveTravelPhotoLocally(photo);
        TravelImage travelImage = new TravelImage();
        travelImage.setFileName(fileName);
        travelImage.setMain(isMain);
        travelImage.setTravel(travel);
        travelImageRepository.save(travelImage);
    }

    private String saveTravelPhotoLocally(MultipartFile photo) {
        String originalFileName = photo.getOriginalFilename();
        if (originalFileName != null) {
            String[] separatedFileName = originalFileName.split("\\.");

            String fileName = UUID.randomUUID() + "." + separatedFileName[separatedFileName.length - 1];
            Path path = Paths.get(travelPhotoStoragePath, fileName);
            try {
                Files.createDirectories(path.getParent());
                try (InputStream inputStream = photo.getInputStream()) {
                    Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                }
                return fileName;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
