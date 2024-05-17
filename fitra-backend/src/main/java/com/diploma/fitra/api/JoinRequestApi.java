package com.diploma.fitra.api;

import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.dto.request.RequestSaveDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/join-requests")
public interface JoinRequestApi {

    @PostMapping("/travel/{travelId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> createRequest(@PathVariable Long travelId,
                                       @RequestBody RequestSaveDto requestSaveDto,
                                       @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/travel/{travelId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<RequestDto> getTravelRequests(@PathVariable Long travelId,
                                       @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<RequestDto> getUserRequests(@PageableDefault Pageable pageable,
                                     @AuthenticationPrincipal UserDetails userDetails);

    @PutMapping("/{requestId}/viewed")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> setRequestAsViewed(@PathVariable Long requestId,
                                            @AuthenticationPrincipal UserDetails userDetails);

    @PostMapping("/{requestId}/approve")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> approveRequest(@PathVariable Long requestId,
                                        @AuthenticationPrincipal UserDetails userDetails);

    @PostMapping("/{requestId}/reject")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> rejectRequest(@PathVariable Long requestId,
                                       @AuthenticationPrincipal UserDetails userDetails);

    @DeleteMapping("/{requestId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> deleteRequest(@PathVariable Long requestId,
                                       @AuthenticationPrincipal UserDetails userDetails);
}
