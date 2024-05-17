package com.diploma.fitra.controller;

import com.diploma.fitra.api.ImageApi;
import com.diploma.fitra.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController implements ImageApi {

    private final ImageService imageService;

    @Override
    public ResponseEntity<byte[]> getUserAvatar(Long userId) {
        byte[] avatar = imageService.getUserAvatar(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(avatar.length);
        return new ResponseEntity<>(avatar, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> setUserAvatar(Long userId, String fileName, UserDetails userDetails) {
        imageService.setUserAvatar(userId, fileName, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<String> getAllUserPhotos(Long userId) {
        return imageService.getAllUserPhotos(userId);
    }

    @Override
    public ResponseEntity<byte[]> getUserPhoto(Long userId, String fileName) {
        byte[] photo = imageService.getUserPhoto(userId, fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(photo.length);
        return new ResponseEntity<>(photo, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> uploadUserPhoto(Long userId, MultipartFile photo, UserDetails userDetails) {
        imageService.uploadUserPhoto(userId, photo, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteUserPhoto(Long userId, String fileName, UserDetails userDetails) {
        imageService.deleteUserPhoto(userId, fileName, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> uploadTravelPhoto(Long travelId, MultipartFile photo, UserDetails userDetails) {
        imageService.uploadTravelPhoto(travelId, photo, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<byte[]> getTravelPhoto(Long travelId, String fileName) {
        byte[] photo = imageService.getTravelPhoto(travelId, fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(photo.length);
        return new ResponseEntity<>(photo, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getTravelMainPhoto(Long travelId) {
        byte[] photo = imageService.getTravelMainPhoto(travelId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(photo.length);
        return new ResponseEntity<>(photo, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> setTravelMainPhoto(Long travelId, String fileName, UserDetails userDetails) {
        imageService.setTravelMainPhoto(travelId, fileName, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<String> getAllTravelPhotos(Long travelId) {
        return imageService.getAllTravelPhotos(travelId);
    }

    @Override
    public ResponseEntity<Void> deleteTravelPhoto(Long travelId, String fileName, UserDetails userDetails) {
        imageService.deleteTravelPhoto(travelId, fileName, userDetails);
        return ResponseEntity.noContent().build();
    }
}
