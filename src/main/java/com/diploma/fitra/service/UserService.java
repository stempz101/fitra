package com.diploma.fitra.service;

import com.diploma.fitra.dto.user.UserSaveDto;
import com.diploma.fitra.model.User;

public interface UserService {

    User saveUser(UserSaveDto userDto);

    User getUser(Long userId);

    void deleteUser(Long userId);
}
