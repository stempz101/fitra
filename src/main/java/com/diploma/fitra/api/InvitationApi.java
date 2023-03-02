package com.diploma.fitra.api;

import com.diploma.fitra.dto.invitation.InvitationDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/invitations")
public interface InvitationApi {

    @PostMapping("/travel/{travelId}/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> createInvitation(@PathVariable Long travelId,
                                          @PathVariable Long userId,
                                          @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<InvitationDto> getInvitations(@PathVariable Long userId,
                                       @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/creator/{creatorId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<InvitationDto> getInvitationsForCreator(@PathVariable Long creatorId,
                                                 @AuthenticationPrincipal UserDetails userDetails);

    @PostMapping("/{invitationId}/approve")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> approveInvitation(@PathVariable Long invitationId,
                                           @AuthenticationPrincipal UserDetails userDetails);

    @PostMapping("/{invitationId}/reject")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> rejectInvitation(@PathVariable Long invitationId,
                                          @AuthenticationPrincipal UserDetails userDetails);

    @DeleteMapping("/{invitationId}/cancel")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> cancelInvitation(@PathVariable Long invitationId,
                                          @AuthenticationPrincipal UserDetails userDetails);
}
