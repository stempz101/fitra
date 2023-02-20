package com.diploma.fitra.service;

import com.diploma.fitra.dto.request.RequestDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CreateRequestService {

    List<RequestDto> getRequests(Authentication auth);

    RequestDto getRequest(Long requestId, Authentication auth);

    void confirmRequest(Long requestId, Authentication auth);

    void rejectRequest(Long requestId, Authentication auth);
}
