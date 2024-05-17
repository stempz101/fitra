package com.diploma.fitra.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/v1/images")
public interface ImageApi {

    @GetMapping("/user/{userId}/avatar")
    ResponseEntity<byte[]> getUserAvatar(@PathVariable Long userId);

    @PutMapping("/user/{userId}/name/{fileName}/avatar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> setUserAvatar(@PathVariable Long userId,
                                       @PathVariable String fileName,
                                       @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/user/{userId}")
    List<String> getAllUserPhotos(@PathVariable Long userId);

    @GetMapping("/user/{userId}/name/{fileName}")
    ResponseEntity<byte[]> getUserPhoto(@PathVariable Long userId, @PathVariable String fileName);

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> uploadUserPhoto(@PathVariable Long userId,
                                         @RequestParam("photo") MultipartFile photo,
                                         @AuthenticationPrincipal UserDetails userDetails);

    @DeleteMapping("/user/{userId}/name/{fileName}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> deleteUserPhoto(@PathVariable Long userId,
                                         @PathVariable String fileName,
                                         @AuthenticationPrincipal UserDetails userDetails);

    @PostMapping("/travel/{travelId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> uploadTravelPhoto(@PathVariable Long travelId,
                                           @RequestParam("photo") MultipartFile photo,
                                           @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/travel/{travelId}/name/{fileName}")
    ResponseEntity<byte[]> getTravelPhoto(@PathVariable Long travelId, @PathVariable String fileName);

    @GetMapping("/travel/{travelId}/main")
    ResponseEntity<byte[]> getTravelMainPhoto(@PathVariable Long travelId);

    @PutMapping("/travel/{travelId}/name/{fileName}/main")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> setTravelMainPhoto(@PathVariable Long travelId,
                                            @PathVariable String fileName,
                                            @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/travel/{travelId}")
    List<String> getAllTravelPhotos(@PathVariable Long travelId);

    @DeleteMapping("/travel/{travelId}/name/{fileName}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> deleteTravelPhoto(@PathVariable Long travelId,
                                           @PathVariable String fileName,
                                           @AuthenticationPrincipal UserDetails userDetails);
}
