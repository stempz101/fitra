package com.diploma.fitra.api;

import com.diploma.fitra.dto.request.RequestDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/create-request")
public interface CreateRequestApi {

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<RequestDto> getRequests(Authentication auth);

    @GetMapping("/{requestId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    RequestDto getRequest(@PathVariable Long requestId, Authentication auth);

    @PostMapping("/{requestId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> confirmRequest(@PathVariable Long requestId, Authentication auth);

    @DeleteMapping("/{requestId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> rejectRequest(@PathVariable Long requestId, Authentication auth);
}
