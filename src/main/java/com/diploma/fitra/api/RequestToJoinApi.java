package com.diploma.fitra.api;

import com.diploma.fitra.dto.request.RequestDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/requests-to-join")
public interface RequestToJoinApi {

    @PostMapping("/travel/{travelId}/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> createRequest(@PathVariable Long travelId, @PathVariable Long userId, Authentication auth);

    @GetMapping("/creator/{creatorId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<RequestDto> getRequests(@PathVariable Long creatorId, Authentication auth);

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<RequestDto> getRequestsForUser(@PathVariable Long userId, Authentication auth);

    @PostMapping("/{requestId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> confirmRequest(@PathVariable Long requestId, Authentication auth);

    @PutMapping("/{requestId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> rejectRequest(@PathVariable Long requestId, Authentication auth);

    @DeleteMapping("/{requestId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> cancelRequest(@PathVariable Long requestId, Authentication auth);
}
