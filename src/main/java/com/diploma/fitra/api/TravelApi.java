package com.diploma.fitra.api;

import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.group.OnUpdate;
import com.diploma.fitra.dto.travel.*;
import com.diploma.fitra.dto.user.UserDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/v1/travels")
public interface TravelApi {

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    TravelDto createTravel(@ModelAttribute @Validated(OnCreate.class) TravelSaveDto travelSaveDto,
                           @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping
    TravelItemsResponse getTravels(@RequestParam(required = false) String name,
                                   @RequestParam(required = false) Long countryId,
                                   @RequestParam(required = false) Long cityId,
                                   @RequestParam(required = false) Long typeId,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam(required = false) Integer peopleFrom,
                                   @RequestParam(required = false) Integer peopleTo,
                                   @PageableDefault Pageable pageable);

    @GetMapping("/user/participating")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    TravelItemsResponse getParticipatingTravels(@PageableDefault Pageable pageable,
                                                @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/user/created")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    TravelItemsResponse getCreatedTravels(@PageableDefault Pageable pageable,
                                          @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/{travelId}")
    @SecurityRequirement(name = "Bearer Authentication")
    TravelDto getTravel(@PathVariable Long travelId,
                        @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/{travelId}/users")
    List<ParticipantDto> getUsers(@PathVariable Long travelId);

    @GetMapping("/{travelId}/invite")
    List<UserDto> getUsersToInvite(@PathVariable Long travelId,
                                   @RequestParam(defaultValue = "") String search,
                                   @PageableDefault Pageable pageable);

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

    @GetMapping("/{travelId}/events")
    List<EventDto> getEvents(@PathVariable Long travelId);

    @PostMapping("/{travelId}/events")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<EventDto> setEvents(@PathVariable Long travelId,
                                   @RequestParam("events") @Valid
                                   @NotBlank(message = "{validation.not_blank.events}") String events,
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
