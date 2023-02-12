package com.diploma.fitra.controller;

import com.diploma.fitra.api.TravelApi;
import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import com.diploma.fitra.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TravelController implements TravelApi {
    private final TravelService travelService;

    @Override
    public TravelDto createTravel(TravelSaveDto travelSaveDto, Authentication auth) {
        return travelService.createTravel(travelSaveDto, auth);
    }

    @Override
    public List<TravelDto> getTravels() {
        return travelService.getTravels();
    }

    @Override
    public List<ParticipantDto> getUsers(Long travelId) {
        return travelService.getUsers(travelId);
    }

    @Override
    public ResponseEntity<Void> removeUser(Long travelId, Long userId, Authentication auth) {
        travelService.removeUser(travelId, userId, auth);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> leaveTravel(Long travelId, Long userId, Authentication auth) {
        travelService.leaveTravel(travelId, userId, auth);
        return ResponseEntity.noContent().build();
    }

    @Override
    public TravelDto updateTravel(Long travelId, TravelSaveDto travelSaveDto, Authentication auth) {
        return travelService.updateTravel(travelId, travelSaveDto, auth);
    }

    @Override
    public ResponseEntity<Void> deleteTravel(Long travelId, Authentication auth) {
        travelService.deleteTravel(travelId, auth);
        return ResponseEntity.noContent().build();
    }
}
