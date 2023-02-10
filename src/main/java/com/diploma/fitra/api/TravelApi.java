package com.diploma.fitra.api;

import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.group.OnUpdate;
import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/travels")
public interface TravelApi {

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    TravelDto createTravel(@RequestBody @Validated(OnCreate.class) TravelSaveDto travelSaveDto);

    @GetMapping
    List<TravelDto> getTravels();

    @GetMapping("/{travelId}/user")
    List<ParticipantDto> getUsers(@PathVariable Long travelId);

    @DeleteMapping("/{travelId}/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> removeUser(@PathVariable Long travelId, @PathVariable Long userId);

    @DeleteMapping("/{travelId}/user/{userId}/leave")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> leaveTravel(@PathVariable Long travelId, @PathVariable Long userId, Authentication auth);

    @PutMapping("/{travelId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    TravelDto updateTravel(@PathVariable Long travelId, @RequestBody @Validated(OnUpdate.class) TravelSaveDto travelSaveDto);

    @DeleteMapping("/{travelId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> deleteTravel(@PathVariable Long travelId);
}
