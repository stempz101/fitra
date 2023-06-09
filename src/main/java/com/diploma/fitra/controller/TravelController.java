package com.diploma.fitra.controller;

import com.diploma.fitra.api.TravelApi;
import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.*;
import com.diploma.fitra.dto.user.UserDto;
import com.diploma.fitra.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TravelController implements TravelApi {
    private final TravelService travelService;

    @Override
    public TravelDto createTravel(TravelSaveDto travelSaveDto, UserDetails userDetails) {
        System.out.println(travelSaveDto);
        return travelService.createTravel(travelSaveDto, userDetails);
    }

    @Override
    public TravelItemsResponse getTravels(String name, Long countryId, Long cityId, Long typeId,
                                      LocalDate startDate, Integer peopleFrom, Integer peopleTo, Pageable pageable) {
        return travelService.getTravels(name, countryId, cityId, typeId, startDate, peopleFrom, peopleTo, pageable);
    }

    @Override
    public TravelItemsResponse getParticipatingTravels(Pageable pageable, UserDetails userDetails) {
        return travelService.getParticipatingTravels(pageable, userDetails);
    }

    @Override
    public TravelItemsResponse getCreatedTravels(Pageable pageable, UserDetails userDetails) {
        return travelService.getCreatedTravels(pageable, userDetails);
    }

    @Override
    public TravelDto getTravel(Long travelId, UserDetails userDetails) {
        return travelService.getTravel(travelId, userDetails);
    }

    @Override
    public List<ParticipantDto> getUsers(Long travelId) {
        return travelService.getUsers(travelId);
    }

    @Override
    public List<UserDto> getUsersToInvite(Long travelId, String search, Pageable pageable) {
        return travelService.getUsersToInvite(travelId, search, pageable);
    }

    @Override
    public ResponseEntity<Void> removeUser(Long travelId, Long userId, UserDetails userDetails) {
        travelService.removeUser(travelId, userId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> leaveTravel(Long travelId, UserDetails userDetails) {
        travelService.leaveTravel(travelId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<EventDto> getEvents(Long travelId) {
        return travelService.getEvents(travelId);
    }

    @Override
    public List<EventDto> setEvents(Long travelId, String events, UserDetails userDetails) {
        return travelService.setEvents(travelId, events, userDetails);
    }

    @Override
    public TravelDto updateTravel(Long travelId, TravelSaveDto travelSaveDto, UserDetails userDetails) {
        return travelService.updateTravel(travelId, travelSaveDto, userDetails);
    }

    @Override
    public List<RouteDto> updateRoute(Long travelId, RouteSaveDto routeSaveDto, UserDetails userDetails) {
        return travelService.updateRoute(travelId, routeSaveDto, userDetails);
    }

    @Override
    public ResponseEntity<Void> deleteTravel(Long travelId, UserDetails userDetails) {
        travelService.deleteTravel(travelId, userDetails);
        return ResponseEntity.noContent().build();
    }
}
