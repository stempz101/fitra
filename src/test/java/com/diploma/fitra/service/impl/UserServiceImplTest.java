package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.user.UserDto;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.model.User;
import com.diploma.fitra.repo.UserRepository;
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

//    @Mock
//    private CountryRepository countryRepository;
//
//    @Mock
//    private CityRepository cityRepository;

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
                hasProperty("city", equalTo(userDto.getCity())),
                hasProperty("isAdmin", equalTo(true))
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
