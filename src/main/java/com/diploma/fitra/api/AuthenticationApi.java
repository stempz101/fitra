package com.diploma.fitra.api;

import com.diploma.fitra.dto.auth.AuthenticationDto;
import com.diploma.fitra.dto.auth.AuthenticationResponseDto;
import com.diploma.fitra.dto.auth.RegisterDto;
import com.diploma.fitra.dto.group.OnCreate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/v1/auth")
public interface AuthenticationApi {

    @PostMapping("/register")
    ResponseEntity<AuthenticationResponseDto> register(@RequestBody @Validated(OnCreate.class) RegisterDto registerDto);

    @PostMapping("/authenticate")
    ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationDto authDto);
}
