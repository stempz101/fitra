package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.user.UserSaveDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.exception.VerificationException;
import com.diploma.fitra.mapper.UserMapper;
import com.diploma.fitra.model.City;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.CityRepository;
import com.diploma.fitra.repo.CountryRepository;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    @Override
    public User saveUser(UserSaveDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ExistenceException(Error.USER_EXISTS_WITH_EMAIL.getMessage());
        } else if (!userDto.getPassword().equals(userDto.getRepeatPassword())) {
            throw new VerificationException(Error.PASSWORD_CONFIRMATION_IS_FAILED.getMessage());
        }

        Country country = countryRepository.findById(userDto.getCountryId())
                .orElseThrow(() -> new NotFoundException(Error.COUNTRY_NOT_FOUND.getMessage()));
        City city = cityRepository.findById(userDto.getCityId()).orElse(null);
        if (city != null && !city.getCountry().getId().equals(country.getId())) {
            throw new BadRequestException(Error.CITY_NOT_IN_COUNTRY.getMessage());
        }

        User user = UserMapper.INSTANCE.fromUserSaveDto(userDto);
        user.setCountry(country);
        user.setCity(city);
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElse(null);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
