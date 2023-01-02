package com.diploma.fitra.api;

import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/travel")
public interface TravelApi {

    @PostMapping
    TravelDto createTravel(@RequestBody @Valid TravelSaveDto travelSaveDto);

    @GetMapping
    List<TravelDto> getTravels();

    @DeleteMapping("/{travelId}")
    ResponseEntity<Void> deleteTravel(@PathVariable Long travelId);
}
