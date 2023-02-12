package com.diploma.fitra.service;

import com.diploma.fitra.dto.user.UserDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers();

    UserDto getUser(Long userId);

    void deleteUser(Long userId, Authentication auth);
}
