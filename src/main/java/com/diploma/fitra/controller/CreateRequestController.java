package com.diploma.fitra.controller;

import com.diploma.fitra.api.CreateRequestApi;
import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.service.CreateRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CreateRequestController implements CreateRequestApi {

    private final CreateRequestService createRequestService;

    @Override
    public List<RequestDto> getRequests(Authentication auth) {
        return createRequestService.getRequests(auth);
    }

    @Override
    public RequestDto getRequest(Long requestId, Authentication auth) {
        return createRequestService.getRequest(requestId, auth);
    }

    @Override
    public ResponseEntity<Void> confirmRequest(Long requestId, Authentication auth) {
        createRequestService.confirmRequest(requestId, auth);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> rejectRequest(Long requestId, Authentication auth) {
        createRequestService.rejectRequest(requestId, auth);
        return ResponseEntity.noContent().build();
    }
}
