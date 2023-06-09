package com.diploma.fitra.service;

import com.diploma.fitra.dto.invitation.InvitationDto;
import com.diploma.fitra.dto.invitation.InvitationSaveDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface InvitationService {

    void createInvitation(Long travelId, Long userId, InvitationSaveDto invitationSaveDto, UserDetails userDetails);

    List<InvitationDto> getUserInvitations(Pageable pageable, UserDetails userDetails);

    List<InvitationDto> getTravelInvitations(Long travelId, UserDetails userDetails);

    void setInviteAsViewed(Long invitationId, UserDetails userDetails);

    void approveInvitation(Long invitationId, UserDetails userDetails);

    void rejectInvitation(Long invitationId, UserDetails userDetails);

    void deleteInvitation(Long invitationId, UserDetails userDetails);
}
