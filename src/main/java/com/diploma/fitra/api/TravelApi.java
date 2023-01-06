package com.diploma.fitra.api;

import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.group.OnUpdate;
import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/travel")
public interface TravelApi {

    @PostMapping
    TravelDto createTravel(@RequestBody @Validated(OnCreate.class) TravelSaveDto travelSaveDto);

    @GetMapping
    List<TravelDto> getTravels();

    @PostMapping("/{travelId}/user/{userId}")
    ParticipantDto addUser(@PathVariable Long travelId, @PathVariable Long userId);

    @GetMapping("/{travelId}/user")
    List<ParticipantDto> getUsers(@PathVariable Long travelId);

    @DeleteMapping("/{travelId}/user/{userId}")
    ResponseEntity<Void> removeUser(@PathVariable Long travelId, @PathVariable Long userId);

    @PutMapping("/{travelId}")
    TravelDto updateTravel(@PathVariable Long travelId, @RequestBody @Validated(OnUpdate.class) TravelSaveDto travelSaveDto);

    @DeleteMapping("/{travelId}")
    ResponseEntity<Void> deleteTravel(@PathVariable Long travelId);
}
