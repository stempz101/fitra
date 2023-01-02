package com.diploma.fitra.controller;

import com.diploma.fitra.api.TravelApi;
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

    public TravelDto createTravel(TravelSaveDto travelSaveDto) {
        return travelService.createTravel(travelSaveDto);
    }

    public List<TravelDto> getTravels() {
        return travelService.getTravels();
    }

    public ResponseEntity<Void> deleteTravel(Long travelId) {
        travelService.deleteTravel(travelId);
        return ResponseEntity.noContent().build();
    }
}
