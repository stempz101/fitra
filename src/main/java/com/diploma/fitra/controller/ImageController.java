package com.diploma.fitra.controller;

import com.diploma.fitra.api.ImageApi;
import com.diploma.fitra.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImageController implements ImageApi {

    private final ImageService imageService;

    @Override
    public ResponseEntity<byte[]> getUserAvatar(String fileName) {
        byte[] avatar = imageService.getUserAvatar(fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(avatar.length);
        return new ResponseEntity<>(avatar, headers, HttpStatus.OK);
    }
}
