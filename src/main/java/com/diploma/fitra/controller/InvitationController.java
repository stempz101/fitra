package com.diploma.fitra.controller;

import com.diploma.fitra.api.InvitationApi;
import com.diploma.fitra.dto.invitation.InvitationDto;
import com.diploma.fitra.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InvitationController implements InvitationApi {

    private final InvitationService invitationService;

    @Override
    public ResponseEntity<Void> createInvitation(Long travelId, Long userId, UserDetails userDetails) {
        invitationService.createInvitation(travelId, userId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<InvitationDto> getInvitations(Long userId, UserDetails userDetails) {
        return invitationService.getInvitations(userId, userDetails);
    }

    @Override
    public List<InvitationDto> getInvitationsForCreator(Long creatorId, UserDetails userDetails) {
        return invitationService.getInvitationsForCreator(creatorId, userDetails);
    }

    @Override
    public ResponseEntity<Void> approveInvitation(Long invitationId, UserDetails userDetails) {
        invitationService.approveInvitation(invitationId, userDetails);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> rejectInvitation(Long invitationId, UserDetails userDetails) {
        invitationService.rejectInvitation(invitationId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> cancelInvitation(Long invitationId, UserDetails userDetails) {
        invitationService.cancelInvitation(invitationId, userDetails);
        return ResponseEntity.noContent().build();
    }
}
