package com.diploma.fitra.controller;

import com.diploma.fitra.api.InvitationApi;
import com.diploma.fitra.dto.invitation.InvitationDto;
import com.diploma.fitra.dto.invitation.InvitationSaveDto;
import com.diploma.fitra.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InvitationController implements InvitationApi {

    private final InvitationService invitationService;

    @Override
    public ResponseEntity<Void> createInvitation(Long travelId, Long userId, InvitationSaveDto invitationSaveDto, UserDetails userDetails) {
        invitationService.createInvitation(travelId, userId, invitationSaveDto, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<InvitationDto> getUserInvitations(Pageable pageable, UserDetails userDetails) {
        return invitationService.getUserInvitations(pageable, userDetails);
    }

    @Override
    public List<InvitationDto> getTravelInvitations(Long travelId, UserDetails userDetails) {
        return invitationService.getTravelInvitations(travelId, userDetails);
    }

    @Override
    public ResponseEntity<Void> setInviteAsViewed(Long invitationId, UserDetails userDetails) {
        invitationService.setInviteAsViewed(invitationId, userDetails);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<Void> deleteInvitation(Long invitationId, UserDetails userDetails) {
        invitationService.deleteInvitation(invitationId, userDetails);
        return ResponseEntity.noContent().build();
    }
}
