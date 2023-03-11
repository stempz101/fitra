package com.diploma.fitra.service.impl;

import com.diploma.fitra.config.email.EmailService;
import com.diploma.fitra.config.security.JwtService;
import com.diploma.fitra.dto.user.*;
import com.diploma.fitra.exception.*;
import com.diploma.fitra.mapper.*;
import com.diploma.fitra.model.City;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.enums.Role;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.CityRepository;
import com.diploma.fitra.repo.CountryRepository;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Override
    public UserDto register(UserSaveDto userSaveDto) {
        if (userRepository.existsByEmail(userSaveDto.getEmail())) {
            throw new ExistenceException(Error.USER_EXISTS_WITH_EMAIL.getMessage());
        } else if (!userSaveDto.getPassword().equals(userSaveDto.getRepeatPassword())) {
            throw new VerificationException(Error.PASSWORD_CONFIRMATION_IS_FAILED.getMessage());
        }

        Country country = countryRepository.findById(userSaveDto.getCountryId())
                .orElseThrow(() -> new NotFoundException(Error.COUNTRY_NOT_FOUND.getMessage()));
        City city = null;
        if (userSaveDto.getCityId() != null) {
            city = cityRepository.findById(userSaveDto.getCityId())
                    .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND.getMessage()));
            if (!city.getCountry().getId().equals(country.getId())) {
                throw new BadRequestException(Error.CITY_NOT_IN_COUNTRY.getMessage());
            }
        }

        User user = AuthenticationMapper.INSTANCE.fromRegisterDto(userSaveDto);
        user.setPassword(passwordEncoder.encode(userSaveDto.getPassword()));
        user.setCountry(country);
        user.setCity(city);
        user.setRole(Role.USER);
        user.setOtp(emailService.generateOtp());
        user = userRepository.save(user);

        emailService.sendOtpEmail(user.getEmail(), user.getOtp());

        return toUserDto(user);
    }

    @Override
    public JwtDto verifyEmail(EmailVerifyDto emailVerifyDto) {
        User user = userRepository.findByEmail(emailVerifyDto.getEmail())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (!emailVerifyDto.getOtp().equals(user.getOtp())) {
            throw new BadRequestException(Error.OTP_DOES_NOT_MATCH.getMessage());
        }

        user.setEnabled(true);
        user = userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return JwtDto.builder().token(jwtToken).build();
    }

    @Override
    public JwtDto authenticate(UserAuthDto authDto) {
        try {
//            UserDetails userDetails = (UserDetails) authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            authDto.getEmail(),
//                            authDto.getPassword()
//                    )
//            ).getPrincipal();
//
//            if (!userDetails.isEnabled()) {
//                throw new ForbiddenException(Error.EMAIL_NOT_VERIFIED.getMessage());
//            }
            User user = userRepository.findByEmail(authDto.getEmail())
                    .orElseThrow(() -> new UnauthorizedException(Error.UNAUTHORIZED.getMessage()));
            if (!passwordEncoder.matches(authDto.getPassword(), user.getPassword())) {
                throw new UnauthorizedException(Error.UNAUTHORIZED.getMessage());
            }

            if (!user.isEnabled()) {
                throw new ForbiddenException(Error.EMAIL_NOT_VERIFIED.getMessage());
            }

            String jwtToken = jwtService.generateToken(user);
            return JwtDto.builder().token(jwtToken).build();
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException(Error.UNAUTHORIZED.getMessage());
        }
    }

    @Override
    public List<UserDto> getUsers(Pageable pageable, UserDetails userDetails) {
        log.info("Getting users");

        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        return userRepository.findAll().stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long userId) {
        log.info("Getting user (id={})", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        return toUserDto(user);
    }

    @Override
    public UserDto updateUserInfo(Long userId, UserInfoSaveDto userInfoSaveDto, UserDetails userDetails) {
        log.info("Updating user (id={}) information", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (!user.getEmail().equals(userDetails.getUsername())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        }
        Country country = countryRepository.findById(userInfoSaveDto.getCountryId())
                .orElseThrow(() -> new NotFoundException(Error.COUNTRY_NOT_FOUND.getMessage()));
        City city = null;
        if (userInfoSaveDto.getCityId() != null) {
            city = cityRepository.findById(userInfoSaveDto.getCityId())
                    .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND.getMessage()));
            if (!city.getCountry().getId().equals(country.getId())) {
                throw new BadRequestException(Error.CITY_NOT_IN_COUNTRY.getMessage());
            }
        }

        user = UpdateMapper.updateUserWithPresentUserInfoSaveDtoFields(user, userInfoSaveDto);
        user.setCountry(country);
        user.setCity(city);
        user = userRepository.save(user);

        log.info("User (id={}) information is updated successfully!", user.getId());
        return toUserDto(user);
    }

    @Override
    public void deleteUser(Long userId, UserDetails userDetails) {
        log.info("Deleting user (id={})", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (user.getEmail().equals(userDetails.getUsername())) {
            throw new BadRequestException(Error.ADMIN_CANT_DELETE_HIMSELF.getMessage());
        }

        userRepository.delete(user);
        log.info("User (id={}) is deleted successfully!", user.getId());
    }

    private UserDto toUserDto(User user) {
        UserDto userDto = UserMapper.INSTANCE.toUserDto(user);
        userDto.setCountry(CountryMapper.INSTANCE.toCountryDto(user.getCountry()));
        userDto.setCity(CityMapper.INSTANCE.toCityDto(user.getCity()));
        if (user.getRole().equals(Role.ADMIN)) {
            userDto.setIsAdmin(true);
        }
        return userDto;
    }
}
