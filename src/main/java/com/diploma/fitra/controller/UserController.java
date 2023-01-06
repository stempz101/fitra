package com.diploma.fitra.controller;

import com.diploma.fitra.api.UserApi;
import com.diploma.fitra.dto.user.UserDto;
import com.diploma.fitra.dto.user.UserSaveDto;
import com.diploma.fitra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService userService;

    public UserDto createUser(UserSaveDto userSaveDto) {
        return userService.createUser(userSaveDto);
    }

    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    public UserDto getUser(Long userId) {
        return userService.getUser(userId);
    }

    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }
}
