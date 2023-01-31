package com.diploma.fitra.controller;

import com.diploma.fitra.api.AuthenticationApi;
import com.diploma.fitra.dto.auth.AuthenticationDto;
import com.diploma.fitra.dto.auth.AuthenticationResponseDto;
import com.diploma.fitra.dto.auth.RegisterDto;
import com.diploma.fitra.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationApi {

    private final AuthenticationService authenticationService;

    @Override
    public ResponseEntity<AuthenticationResponseDto> register(RegisterDto registerDto) {
        return ResponseEntity.ok(authenticationService.register(registerDto));
    }

    @Override
    public ResponseEntity<AuthenticationResponseDto> authenticate(AuthenticationDto authDto) {
        return ResponseEntity.ok(authenticationService.authenticate(authDto));
    }
}
