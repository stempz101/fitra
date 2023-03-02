package com.diploma.fitra.service;

import com.diploma.fitra.dto.request.RequestDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JoinRequestService {

    void createRequest(Long travelId, Long userId, UserDetails userDetails);

    List<RequestDto> getRequests(Long creatorId, UserDetails userDetails);

    List<RequestDto> getRequestsForUser(Long userId, UserDetails userDetails);

    void approveRequest(Long requestId, UserDetails userDetails);

    void rejectRequest(Long requestId, UserDetails userDetails);

    void cancelRequest(Long requestId, UserDetails userDetails);
}
