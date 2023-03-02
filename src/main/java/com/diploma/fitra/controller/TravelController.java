package com.diploma.fitra.controller;

import com.diploma.fitra.api.TravelApi;
import com.diploma.fitra.dto.travel.*;
import com.diploma.fitra.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TravelController implements TravelApi {
    private final TravelService travelService;

    @Override
    public TravelDto createTravel(TravelSaveDto travelSaveDto, UserDetails userDetails) {
        return travelService.createTravel(travelSaveDto, userDetails);
    }

    @Override
    public List<TravelDto> getTravels(Pageable pageable) {
        return travelService.getTravels(pageable);
    }

    @Override
    public List<TravelDto> getTravelsForUser(Pageable pageable, UserDetails userDetails) {
        return travelService.getTravelsForUser(pageable, userDetails);
    }

    @Override
    public List<ParticipantDto> getUsers(Long travelId) {
        return travelService.getUsers(travelId);
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
