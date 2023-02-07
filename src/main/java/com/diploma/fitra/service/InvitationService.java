package com.diploma.fitra.service;

import com.diploma.fitra.dto.invitation.InvitationDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface InvitationService {

    void createInvitation(Long travelId, Long userId, Authentication auth);

    List<InvitationDto> getInvitations(Long userId, Authentication auth);

    void confirmInvitation(Long travelId, Long userId, Authentication auth);

    void rejectInvitation(Long travelId, Long userId, Authentication auth);

    void cancelInvitation(Long travelId, Long userId, Authentication auth);
}
