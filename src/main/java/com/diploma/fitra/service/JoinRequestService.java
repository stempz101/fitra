package com.diploma.fitra.service;

import com.diploma.fitra.dto.request.RequestDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface JoinRequestService {

    void createRequest(Long travelId, Long userId, Authentication auth);

    List<RequestDto> getRequests(Long creatorId, Authentication auth);

    List<RequestDto> getRequestsForUser(Long userId, Authentication auth);

    void approveRequest(Long requestId, Authentication auth);

    void rejectRequest(Long requestId, Authentication auth);

    void cancelRequest(Long requestId, Authentication auth);
}
