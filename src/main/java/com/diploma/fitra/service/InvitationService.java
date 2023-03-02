package com.diploma.fitra.service;

import com.diploma.fitra.dto.invitation.InvitationDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface InvitationService {

    void createInvitation(Long travelId, Long userId, Authentication auth);

    List<InvitationDto> getInvitations(Long userId, Authentication auth);

    List<InvitationDto> getInvitationsForCreator(Long creatorId, Authentication auth);

    void approveInvitation(Long invitationId, Authentication auth);

    void rejectInvitation(Long invitationId, Authentication auth);

    void cancelInvitation(Long invitationId, Authentication auth);
}
