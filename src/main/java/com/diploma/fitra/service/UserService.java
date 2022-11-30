package com.diploma.fitra.service;

import com.diploma.fitra.model.User;

public interface UserService {

    User saveUser(User user);

    void deleteUser(Long id);
}
