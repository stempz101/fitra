package com.diploma.fitra.api;

import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.group.OnUpdate;
import com.diploma.fitra.dto.travel.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/travels")
public interface TravelApi {

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    TravelDto createTravel(@RequestBody @Validated(OnCreate.class) TravelSaveDto travelSaveDto,
                           @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping
    List<TravelDto> getTravels(@PageableDefault Pageable pageable);

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<TravelDto> getTravelsForUser(@PageableDefault Pageable pageable,
                                      @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/{travelId}/users")
    List<ParticipantDto> getUsers(@PathVariable Long travelId);

    @DeleteMapping("/{travelId}/users/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> removeUser(@PathVariable Long travelId,
                                    @PathVariable Long userId,
                                    @AuthenticationPrincipal UserDetails userDetails);

    @DeleteMapping("/{travelId}/leave")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> leaveTravel(@PathVariable Long travelId,
                                     @AuthenticationPrincipal UserDetails userDetails);

    @PutMapping("/{travelId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    TravelDto updateTravel(@PathVariable Long travelId,
                           @RequestBody @Validated(OnUpdate.class) TravelSaveDto travelSaveDto,
                           @AuthenticationPrincipal UserDetails userDetails);

    @PutMapping("/{travelId}/route")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<RouteDto> updateRoute(@PathVariable Long travelId,
                               @RequestBody @Validated(OnUpdate.class) RouteSaveDto routeSaveDto,
                               @AuthenticationPrincipal UserDetails userDetails);

    @DeleteMapping("/{travelId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> deleteTravel(@PathVariable Long travelId,
                                      @AuthenticationPrincipal UserDetails userDetails);
}
