package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.RequestMapper;
import com.diploma.fitra.model.CreateRequest;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.enums.Status;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.CreateRequestRepository;
import com.diploma.fitra.repo.TravelRepository;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.service.CreateRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateRequestServiceImpl implements CreateRequestService {

    private final CreateRequestRepository createRequestRepository;
    private final UserRepository userRepository;
    private final TravelRepository travelRepository;

    @Override
    public List<RequestDto> getRequests(Authentication auth) {
        log.info("Getting requests to create travel");

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        List<RequestDto> requestDtoList;
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            User creator = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
            requestDtoList = createRequestRepository.findAllByTravel_Creator(creator).stream()
                    .map(RequestMapper.INSTANCE::toRequestDto)
                    .collect(Collectors.toList());
        } else {
            requestDtoList = createRequestRepository.findAll().stream()
                    .map(RequestMapper.INSTANCE::toRequestDto)
                    .collect(Collectors.toList());
        }

        return requestDtoList;
    }

    @Override
    public RequestDto getRequest(Long requestId, Authentication auth) {
        log.info("Getting request (id={})", requestId);

        CreateRequest request = createRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(Error.REQUEST_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
                !request.getTravel().getCreator().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        return RequestMapper.INSTANCE.toRequestDto(request);
    }

    @Override
    @Transactional
    public void confirmRequest(Long requestId, Authentication auth) {
        log.info("Request (id={}) confirmation", requestId);

        CreateRequest request = createRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(Error.REQUEST_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (request.getStatus().equals(Status.CONFIRMED)) {
            throw new BadRequestException(Error.REQUEST_IS_CONFIRMED.getMessage());
        }

        request.getTravel().setConfirmed(true);
        request.setStatus(Status.CONFIRMED);
        createRequestRepository.save(request);

        log.info("Request (id={}) is confirmed successfully", requestId);
    }

    @Override
    @Transactional
    public void rejectRequest(Long requestId, Authentication auth) {
        log.info("Request (id={}) rejection", requestId);

        CreateRequest request = createRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(Error.REQUEST_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) &&
                !request.getTravel().getCreator().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (request.getStatus().equals(Status.CONFIRMED)) {
            throw new BadRequestException(Error.REQUEST_IS_CONFIRMED.getMessage());
        }

        travelRepository.delete(request.getTravel());

        log.info("Request (id={}) is rejected successfully", requestId);
    }
}
