package com.diploma.fitra.controller;

import com.diploma.fitra.api.RequestToJoinApi;
import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.service.RequestToJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestToJoinController implements RequestToJoinApi {

    private final RequestToJoinService requestService;

    @Override
    public ResponseEntity<Void> createRequest(Long travelId, Long userId, Authentication auth) {
        requestService.createRequest(travelId, userId, auth);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<RequestDto> getRequests(Long creatorId, Authentication auth) {
        return requestService.getRequests(creatorId, auth);
    }

    @Override
    public List<RequestDto> getRequestsForUser(Long userId, Authentication auth) {
        return requestService.getRequestsForUser(userId, auth);
    }

    @Override
    public ResponseEntity<Void> confirmRequest(Long requestId, Authentication auth) {
        requestService.confirmRequest(requestId, auth);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> rejectRequest(Long requestId, Authentication auth) {
        requestService.rejectRequest(requestId, auth);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> cancelRequest(Long requestId, Authentication auth) {
        requestService.cancelRequest(requestId, auth);
        return ResponseEntity.noContent().build();
    }
}
