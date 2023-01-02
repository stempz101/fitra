package com.diploma.fitra.service;

import com.diploma.fitra.dto.user.UserDto;
import com.diploma.fitra.dto.user.UserSaveDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserSaveDto userSaveDto);

    List<UserDto> getUsers();

    UserDto getUser(Long userId);

    void deleteUser(Long userId);
}
