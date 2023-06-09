package com.diploma.fitra.api;

import com.diploma.fitra.dto.invitation.InvitationDto;
import com.diploma.fitra.dto.invitation.InvitationSaveDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
                                          @RequestBody InvitationSaveDto invitationSaveDto,
                                          @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<InvitationDto> getUserInvitations(@PageableDefault Pageable pageable,
                                           @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/travel/{travelId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<InvitationDto> getTravelInvitations(@PathVariable Long travelId,
                                             @AuthenticationPrincipal UserDetails userDetails);

    @PutMapping("/{invitationId}/viewed")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> setInviteAsViewed(@PathVariable Long invitationId,
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

    @DeleteMapping("/{invitationId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> deleteInvitation(@PathVariable Long invitationId,
                                          @AuthenticationPrincipal UserDetails userDetails);
}
