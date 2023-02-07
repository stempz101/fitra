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
        return ResponseEntity.ok().build();
    }

    @Override
    public List<InvitationDto> getInvitations(Long userId, Authentication auth) {
        return invitationService.getInvitations(userId, auth);
    }

    @Override
    public ResponseEntity<Void> confirmInvitation(Long travelId, Long userId, Authentication auth) {
        invitationService.confirmInvitation(travelId, userId, auth);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> rejectInvitation(Long travelId, Long userId, Authentication auth) {
        invitationService.rejectInvitation(travelId, userId, auth);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> cancelInvitation(Long travelId, Long userId, Authentication auth) {
        invitationService.cancelInvitation(travelId, userId, auth);
        return ResponseEntity.ok().build();
    }
}
