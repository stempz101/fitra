package com.diploma.fitra.controller;

import com.diploma.fitra.api.UserApi;
import com.diploma.fitra.dto.user.UserDto;
import com.diploma.fitra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    @Override
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @Override
    public UserDto getUser(Long userId) {
        return userService.getUser(userId);
    }

    @Override
    public void deleteUser(Long userId, Authentication auth) {
        userService.deleteUser(userId, auth);
    }
}
