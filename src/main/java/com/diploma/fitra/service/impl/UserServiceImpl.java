package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.user.UserDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.CityMapper;
import com.diploma.fitra.mapper.CountryMapper;
import com.diploma.fitra.mapper.UserMapper;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.enums.Role;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
//    private final CountryRepository countryRepository;
//    private final CityRepository cityRepository;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        return toUserDto(user);
    }

    @Override
    public void deleteUser(Long userId, UserDetails userDetails) {
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (user.getEmail().equals(userDetails.getUsername())) {
            throw new BadRequestException(Error.ADMIN_CANT_DELETE_HIMSELF.getMessage());
        }

        userRepository.delete(user);
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
