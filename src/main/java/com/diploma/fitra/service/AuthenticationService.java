package com.diploma.fitra.service;

import com.diploma.fitra.dto.auth.AuthenticationDto;
import com.diploma.fitra.dto.auth.AuthenticationResponseDto;
import com.diploma.fitra.dto.auth.RegisterDto;
import org.springframework.http.ProblemDetail;

public interface AuthenticationService {

    AuthenticationResponseDto register(RegisterDto registerDto);

    AuthenticationResponseDto authenticate(AuthenticationDto authDto);
}
