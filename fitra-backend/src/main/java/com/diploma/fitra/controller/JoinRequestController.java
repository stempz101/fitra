package com.diploma.fitra.controller;

import com.diploma.fitra.api.JoinRequestApi;
import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.dto.request.RequestSaveDto;
import com.diploma.fitra.service.JoinRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class JoinRequestController implements JoinRequestApi {

    private final JoinRequestService requestService;

    @Override
    public ResponseEntity<Void> createRequest(Long travelId, RequestSaveDto requestSaveDto, UserDetails userDetails) {
        requestService.createRequest(travelId, requestSaveDto, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<RequestDto> getTravelRequests(Long travelId, UserDetails userDetails) {
        return requestService.getTravelRequests(travelId, userDetails);
    }

    @Override
    public List<RequestDto> getUserRequests(Pageable pageable, UserDetails userDetails) {
        return requestService.getUserRequests(pageable, userDetails);
    }

    @Override
    public ResponseEntity<Void> setRequestAsViewed(Long requestId, UserDetails userDetails) {
        requestService.setRequestAsViewed(requestId, userDetails);
        return ResponseEntity.noContent().build();
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
    public ResponseEntity<Void> deleteRequest(Long requestId, UserDetails userDetails) {
        requestService.deleteRequest(requestId, userDetails);
        return ResponseEntity.noContent().build();
    }
}
