package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.user.UserDto;
import com.diploma.fitra.dto.user.UserSaveDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.exception.VerificationException;
import com.diploma.fitra.mapper.CityMapper;
import com.diploma.fitra.mapper.CountryMapper;
import com.diploma.fitra.model.City;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.model.User;
import com.diploma.fitra.repo.CityRepository;
import com.diploma.fitra.repo.CountryRepository;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.test.util.CityDataTest;
import com.diploma.fitra.test.util.CountryDataTest;
import com.diploma.fitra.test.util.UserDataTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityRepository cityRepository;

    @Test
    void createUserTest() {
        UserSaveDto userSaveDto = UserDataTest.getUserSaveDto1();
        User user = UserDataTest.getUser1();
        Country country = CountryDataTest.getCountry1();
        City city = CityDataTest.getCity1();

        when(countryRepository.findById(any())).thenReturn(Optional.of(country));
        when(cityRepository.findById(any())).thenReturn(Optional.of(city));
        when(userRepository.save(any())).thenReturn(user);
        UserDto result = userService.createUser(userSaveDto);

        assertThat(result, allOf(
                hasProperty("id", equalTo(user.getId())),
                hasProperty("firstName", equalTo(userSaveDto.getFirstName())),
                hasProperty("lastName", equalTo(userSaveDto.getLastName())),
                hasProperty("about", equalTo(userSaveDto.getAbout())),
                hasProperty("country", equalTo(CountryMapper.INSTANCE.toCountryDto(country))),
                hasProperty("city", equalTo(CityMapper.INSTANCE.toCityDto(city)))
        ));
    }

    @Test
    void createUserWithExistenceExceptionTest() {
        UserSaveDto userSaveDto = UserDataTest.getUserSaveDto1();

        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertThrows(ExistenceException.class, () -> userService.createUser(userSaveDto));
    }

    @Test
    void createUserWithVerificationExceptionTest() {
        UserSaveDto userSaveDto = UserDataTest.getUserSaveDto1();
        userSaveDto.setRepeatPassword("asdasdasdasd");

        assertThrows(VerificationException.class, () -> userService.createUser(userSaveDto));
    }

    @Test
    void createUserWithCountryNotFoundException() {
        UserSaveDto userSaveDto = UserDataTest.getUserSaveDto1();

        when(countryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.createUser(userSaveDto));
    }

    @Test
    void createUserWithBadRequestException() {
        UserSaveDto userSaveDto = UserDataTest.getUserSaveDto1();
        Country country = CountryDataTest.getCountry1();
        City city = CityDataTest.getCity1();
        city.setCountry(CountryDataTest.getCountry2());

        when(countryRepository.findById(any())).thenReturn(Optional.of(country));
        when(cityRepository.findById(any())).thenReturn(Optional.of(city));

        assertThrows(BadRequestException.class, () -> userService.createUser(userSaveDto));
    }

    @Test
    void getUsersTest() {
        List<User> users = new ArrayList<>();
        users.add(UserDataTest.getUser1());
        users.add(UserDataTest.getUser2());
        users.add(UserDataTest.getUser3());
        users.add(UserDataTest.getUser4());

        when(userRepository.findAll()).thenReturn(users);
        List<UserDto> result = userService.getUsers();

        assertThat(result, hasSize(users.size()));
        assertThat(result, hasItems(
                UserDataTest.getUserDto1(),
                UserDataTest.getUserDto2(),
                UserDataTest.getUserDto3(),
                UserDataTest.getUserDto4()
        ));
    }

    @Test
    void getUserTest() {
        User user = UserDataTest.getUser1();
        UserDto userDto = UserDataTest.getUserDto1();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        UserDto result = userService.getUser(user.getId());

        assertThat(result, allOf(
                hasProperty("id", equalTo(user.getId())),
                hasProperty("firstName", equalTo(user.getFirstName())),
                hasProperty("lastName", equalTo(user.getLastName())),
                hasProperty("about", equalTo(user.getAbout())),
                hasProperty("country", equalTo(userDto.getCountry())),
                hasProperty("city", equalTo(userDto.getCity()))
        ));
    }

    @Test
    void getUserTestWithNotFoundExceptionTest() {
        User user = UserDataTest.getUser1();

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUser(user.getId()));
    }

    @Test
    void deleteUserTest() {
        User user = UserDataTest.getUser1();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).delete(any());
    }

    @Test
    void deleteUserWithNotFoundExceptionTest() {
        User user = UserDataTest.getUser1();

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.deleteUser(user.getId()));
    }
}
