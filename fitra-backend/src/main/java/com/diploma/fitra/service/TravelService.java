package com.diploma.fitra.service;

import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.*;
import com.diploma.fitra.dto.user.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;

public interface TravelService {

    TravelDto createTravel(TravelSaveDto travelSaveDto, UserDetails userDetails);

    TravelItemsResponse getTravels(String name, Long countryId, Long cityId, Long typeId,
                               LocalDate startDate, Integer peopleFrom, Integer peopleTo, Pageable pageable);

    TravelItemsResponse getParticipatingTravels(Pageable pageable, UserDetails userDetails);

    TravelItemsResponse getCreatedTravels(Pageable pageable, UserDetails userDetails);

    TravelDto getTravel(Long travelId, UserDetails userDetails);

    List<ParticipantDto> getUsers(Long travelId);

    List<UserDto> getUsersToInvite(Long travelId, String search, Pageable pageable);

    void removeUser(Long travelId, Long userId, UserDetails userDetails);

    void leaveTravel(Long travelId, UserDetails userDetails);

    List<EventDto> getEvents(Long travelId);

    List<EventDto> setEvents(Long travelId, String events, UserDetails userDetails);

    TravelDto updateTravel(Long travelId, TravelSaveDto travelSaveDto, UserDetails userDetails);

    List<RouteDto> updateRoute(Long travelId, RouteSaveDto routeSaveDto, UserDetails userDetails);

    void deleteTravel(Long travelId, UserDetails userDetails);
}
