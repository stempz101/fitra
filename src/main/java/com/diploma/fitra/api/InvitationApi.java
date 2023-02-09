package com.diploma.fitra.api;

import com.diploma.fitra.dto.invitation.InvitationDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/invitations")
public interface InvitationApi {

    @PostMapping("/travel/{travelId}/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> createInvitation(@PathVariable Long travelId, @PathVariable Long userId, Authentication auth);

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<InvitationDto> getInvitations(@PathVariable Long userId, Authentication auth);

    @GetMapping("/user/{userId}/creator")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<InvitationDto> getInvitationsForCreator(@PathVariable Long userId, Authentication auth);

    @PostMapping("/{invitationId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> confirmInvitation(@PathVariable Long invitationId, Authentication auth);

    @PutMapping("/{invitationId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> rejectInvitation(@PathVariable Long invitationId, Authentication auth);

    @DeleteMapping("/{invitationId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> cancelInvitation(@PathVariable Long invitationId, Authentication auth);
}