package com.diploma.fitra.controller;

import com.diploma.fitra.api.InvitationApi;
import com.diploma.fitra.dto.invitation.InvitationDto;
import com.diploma.fitra.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InvitationController implements InvitationApi {

    private final InvitationService invitationService;

    @Override
    public ResponseEntity<Void> createInvitation(Long travelId, Long userId, Authentication auth) {
        invitationService.createInvitation(travelId, userId, auth);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<InvitationDto> getInvitations(Long userId, Authentication auth) {
        return invitationService.getInvitations(userId, auth);
    }

    @Override
    public List<InvitationDto> getInvitationsForCreator(Long userId, Authentication auth) {
        return invitationService.getInvitationsForCreator(userId, auth);
    }

    @Override
    public ResponseEntity<Void> confirmInvitation(Long invitationId, Authentication auth) {
        invitationService.confirmInvitation(invitationId, auth);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> rejectInvitation(Long invitationId, Authentication auth) {
        invitationService.rejectInvitation(invitationId, auth);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> cancelInvitation(Long invitationId, Authentication auth) {
        invitationService.cancelInvitation(invitationId, auth);
        return ResponseEntity.noContent().build();
    }
}
