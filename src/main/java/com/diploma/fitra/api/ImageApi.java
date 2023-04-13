package com.diploma.fitra.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/images")
public interface ImageApi {

    @GetMapping("/{fileName}/avatar")
    ResponseEntity<byte[]> getUserAvatar(@PathVariable String fileName);
}
