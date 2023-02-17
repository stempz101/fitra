package com.diploma.fitra.controller;

import com.diploma.fitra.api.RequestToCreateApi;
import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.service.RequestToCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestToCreateController implements RequestToCreateApi {

    private final RequestToCreateService requestToCreateService;

    @Override
    public List<RequestDto> getRequests(Authentication auth) {
        return requestToCreateService.getRequests(auth);
    }

    @Override
    public RequestDto getRequest(Long requestId, Authentication auth) {
        return requestToCreateService.getRequest(requestId, auth);
    }

    @Override
    public ResponseEntity<Void> confirmRequest(Long requestId, Authentication auth) {
        requestToCreateService.confirmRequest(requestId, auth);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> rejectRequest(Long requestId, Authentication auth) {
        requestToCreateService.rejectRequest(requestId, auth);
        return ResponseEntity.noContent().build();
    }
}
