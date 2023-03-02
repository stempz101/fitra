package com.diploma.fitra.service;

import com.diploma.fitra.dto.invitation.InvitationDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface InvitationService {

    void createInvitation(Long travelId, Long userId, UserDetails userDetails);

    List<InvitationDto> getInvitations(Long userId, UserDetails userDetails);

    List<InvitationDto> getInvitationsForCreator(Long creatorId, UserDetails userDetails);

    void approveInvitation(Long invitationId, UserDetails userDetails);

    void rejectInvitation(Long invitationId, UserDetails userDetails);

    void cancelInvitation(Long invitationId, UserDetails userDetails);
}
