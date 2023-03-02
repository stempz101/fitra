package com.diploma.fitra.controller;

import com.diploma.fitra.api.JoinRequestApi;
import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.service.JoinRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JoinRequestController implements JoinRequestApi {

    private final JoinRequestService requestService;

    @Override
    public ResponseEntity<Void> createRequest(Long travelId, Long userId, UserDetails userDetails) {
        requestService.createRequest(travelId, userId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<RequestDto> getRequests(Long creatorId, UserDetails userDetails) {
        return requestService.getRequests(creatorId, userDetails);
    }

    @Override
    public List<RequestDto> getRequestsForUser(Long userId, UserDetails userDetails) {
        return requestService.getRequestsForUser(userId, userDetails);
    }

    @Override
    public ResponseEntity<Void> approveRequest(Long requestId, UserDetails userDetails) {
        requestService.approveRequest(requestId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> rejectRequest(Long requestId, UserDetails userDetails) {
        requestService.rejectRequest(requestId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> cancelRequest(Long requestId, UserDetails userDetails) {
        requestService.cancelRequest(requestId, userDetails);
        return ResponseEntity.noContent().build();
    }
}
