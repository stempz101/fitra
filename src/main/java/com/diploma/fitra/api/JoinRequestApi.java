package com.diploma.fitra.api;

import com.diploma.fitra.dto.request.RequestDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/join-request")
public interface JoinRequestApi {

    @PostMapping("/travel/{travelId}/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> createRequest(@PathVariable Long travelId,
                                       @PathVariable Long userId,
                                       @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/creator/{creatorId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<RequestDto> getRequests(@PathVariable Long creatorId,
                                 @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<RequestDto> getRequestsForUser(@PathVariable Long userId,
                                        @AuthenticationPrincipal UserDetails userDetails);

    @PostMapping("/{requestId}/approve")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> approveRequest(@PathVariable Long requestId,
                                        @AuthenticationPrincipal UserDetails userDetails);

    @PostMapping("/{requestId}/reject")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> rejectRequest(@PathVariable Long requestId,
                                       @AuthenticationPrincipal UserDetails userDetails);

    @DeleteMapping("/{requestId}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> cancelRequest(@PathVariable Long requestId,
                                       @AuthenticationPrincipal UserDetails userDetails);
}
