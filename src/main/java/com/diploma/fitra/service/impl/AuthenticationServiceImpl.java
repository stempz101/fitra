package com.diploma.fitra.service.impl;

import com.diploma.fitra.config.security.JwtService;
import com.diploma.fitra.dto.auth.AuthenticationDto;
import com.diploma.fitra.dto.auth.AuthenticationResponseDto;
import com.diploma.fitra.dto.auth.RegisterDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.exception.VerificationException;
import com.diploma.fitra.mapper.AuthenticationMapper;
import com.diploma.fitra.model.City;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.model.enums.Role;
import com.diploma.fitra.repo.CityRepository;
import com.diploma.fitra.repo.CountryRepository;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDto register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new ExistenceException(Error.USER_EXISTS_WITH_EMAIL.getMessage());
        } else if (!registerDto.getPassword().equals(registerDto.getRepeatPassword())) {
            throw new VerificationException(Error.PASSWORD_CONFIRMATION_IS_FAILED.getMessage());
        }

        Country country = countryRepository.findById(registerDto.getCountryId())
                .orElseThrow(() -> new NotFoundException(Error.COUNTRY_NOT_FOUND.getMessage()));
        City city = cityRepository.findById(registerDto.getCityId()).orElse(null);
        if (city != null && !city.getCountry().getId().equals(country.getId())) {
            throw new BadRequestException(Error.CITY_NOT_IN_COUNTRY.getMessage());
        }

        User user = AuthenticationMapper.INSTANCE.fromRegisterDto(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setCountry(country);
        user.setCity(city);
        user.setRole(Role.USER);
        user = userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponseDto authenticate(AuthenticationDto authDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDto.getEmail(),
                        authDto.getPassword()
                )
        );
        User user = userRepository.findByEmail(authDto.getEmail())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }
}
