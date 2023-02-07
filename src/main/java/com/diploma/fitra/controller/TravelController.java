package com.diploma.fitra.controller;

import com.diploma.fitra.api.TravelApi;
import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import com.diploma.fitra.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TravelController implements TravelApi {
    private final TravelService travelService;

    @Override
    public TravelDto createTravel(TravelSaveDto travelSaveDto) {
        return travelService.createTravel(travelSaveDto);
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
    public ResponseEntity<Void> removeUser(Long travelId, Long userId) {
        travelService.removeUser(travelId, userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public TravelDto updateTravel(Long travelId, TravelSaveDto travelSaveDto) {
        return travelService.updateTravel(travelId, travelSaveDto);
    }

    @Override
    public ResponseEntity<Void> deleteTravel(Long travelId) {
        travelService.deleteTravel(travelId);
        return ResponseEntity.noContent().build();
    }
}
