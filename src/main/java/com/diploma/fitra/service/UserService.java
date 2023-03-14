package com.diploma.fitra.service;

import com.diploma.fitra.dto.user.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    UserDto register(UserSaveDto userSaveDto);

    JwtDto confirmRegistration(String token);

    JwtDto authenticate(UserAuthDto authDto);

    List<UserDto> getUsers(Pageable pageable, UserDetails userDetails);

    UserDto getUser(Long userId);

    UserDto updateUserInfo(Long userId, UserInfoSaveDto userInfoSaveDto, UserDetails userDetails);

    void updateUserEmail(Long userId, UserEmailSaveDto userEmailSaveDto, UserDetails userDetails);

    void confirmEmail(String token);

    void deleteUser(Long userId, UserDetails userDetails);
}
