package com.diploma.fitra.service.impl;

import com.diploma.fitra.config.email.EmailService;
import com.diploma.fitra.config.security.JwtService;
import com.diploma.fitra.dto.user.*;
import com.diploma.fitra.exception.*;
import com.diploma.fitra.mapper.*;
import com.diploma.fitra.model.City;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.model.EmailUpdate;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.enums.Role;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.CityRepository;
import com.diploma.fitra.repo.CountryRepository;
import com.diploma.fitra.repo.EmailUpdateRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final EmailUpdateRepository emailUpdateRepository;
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

        User user = UserMapper.INSTANCE.fromUserSaveDto(userSaveDto);
        user.setPassword(passwordEncoder.encode(userSaveDto.getPassword()));
        user.setCountry(country);
        user.setCity(city);
        user.setRole(Role.USER);
        user.setConfirmToken(UUID.randomUUID().toString());
        user.setConfirmTokenExpiration(LocalDateTime.now().plusHours(1L));
        user = userRepository.save(user);

        emailService.sendRegistrationConfirmationLink(user);

        return toUserDto(user);
    }

    @Override
    public JwtDto confirmRegistration(String token) {
        User user = userRepository.findByConfirmToken(token)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));

        if (user.isEnabled()) {
            throw new BadRequestException(Error.EMAIL_CONFIRMED.getMessage());
        } else if (LocalDateTime.now().isAfter(user.getConfirmTokenExpiration())) {
            throw new UnauthorizedException(Error.EMAIL_CONFIRMATION_EXPIRED.getMessage());
        }

        user.setEnabled(true);
        user = userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return JwtDto.builder().token(jwtToken).build();
    }

    @Override
    public JwtDto authenticate(UserAuthDto authDto) {
        try {
            User user = userRepository.findByEmail(authDto.getEmail())
                    .orElseThrow(() -> new UnauthorizedException(Error.UNAUTHORIZED.getMessage()));
            if (!passwordEncoder.matches(authDto.getPassword(), user.getPassword())) {
                throw new UnauthorizedException(Error.UNAUTHORIZED.getMessage());
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
    public void updateUserEmail(Long userId, UserEmailSaveDto userEmailSaveDto, UserDetails userDetails) {
        log.info("Updating user (id={}) email", userId);

        User user = (User) userDetails;
        if (!userId.equals(user.getId())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        } else if (user.getEmail().equals(userEmailSaveDto.getEmail())) {
            throw new BadRequestException(Error.EMAIL_IS_CURRENT.getMessage());
        } else if (userRepository.existsByEmail(userEmailSaveDto.getEmail())) {
            throw new BadRequestException(Error.USER_EXISTS_WITH_EMAIL.getMessage());
        }

        Optional<EmailUpdate> emailUpdateOpt = emailUpdateRepository.findByUser(user);
        EmailUpdate emailUpdate;
        LocalDateTime currentTime = LocalDateTime.now();
        if (emailUpdateOpt.isPresent()) {
            emailUpdate = emailUpdateOpt.get();
            if (!currentTime.isAfter(emailUpdate.getConfirmTokenExpiration())) {
                throw new ConflictException(Error.EMAIL_CAN_NOT_BE_UPDATED.getMessage());
            }
            emailUpdate.setEmail(userEmailSaveDto.getEmail());
            emailUpdate.setConfirmToken(UUID.randomUUID().toString());
            emailUpdate.setConfirmTokenExpiration(currentTime.plusHours(1L));
        } else {
            emailUpdate = new EmailUpdate();
            emailUpdate.setUser(user);
            emailUpdate.setEmail(userEmailSaveDto.getEmail());
            emailUpdate.setConfirmToken(UUID.randomUUID().toString());
            emailUpdate.setConfirmTokenExpiration(LocalDateTime.now().plusHours(1L));
        }

        emailUpdate = emailUpdateRepository.save(emailUpdate);

        emailService.sendNewEmailConfirmationLink(user, emailUpdate);

        log.info("User (id={}) email verification is sent to the new specified email", user.getId());
    }

    @Override
    @Transactional
    public void confirmEmail(String token) {
        log.info("Confirming new email");

        EmailUpdate emailUpdate = emailUpdateRepository.findByConfirmToken(token)
                .orElseThrow(() -> new NotFoundException(Error.EMAIL_UPDATE_NOT_FOUND.getMessage()));

        if (LocalDateTime.now().isAfter(emailUpdate.getConfirmTokenExpiration())) {
            throw new UnauthorizedException(Error.EMAIL_CONFIRMATION_EXPIRED.getMessage());
        }

        User user = emailUpdate.getUser();
        user.setEmail(emailUpdate.getEmail());
        userRepository.save(user);

        emailUpdateRepository.delete(emailUpdate);

        log.info("User (id={}) email is updated successfully!", user.getId());
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
