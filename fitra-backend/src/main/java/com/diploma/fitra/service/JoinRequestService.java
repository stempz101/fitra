package com.diploma.fitra.service;

import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.dto.request.RequestSaveDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JoinRequestService {

    void createRequest(Long travelId, RequestSaveDto requestSaveDto, UserDetails userDetails);

    List<RequestDto> getTravelRequests(Long travelId, UserDetails userDetails);

    List<RequestDto> getUserRequests(Pageable pageable, UserDetails userDetails);

    void setRequestAsViewed(Long requestId, UserDetails userDetails);

    void approveRequest(Long requestId, UserDetails userDetails);

    void rejectRequest(Long requestId, UserDetails userDetails);

    void deleteRequest(Long requestId, UserDetails userDetails);
}
