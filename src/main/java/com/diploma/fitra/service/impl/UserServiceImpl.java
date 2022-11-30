package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.user.UserSaveDto;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.VerificationException;
import com.diploma.fitra.mapper.UserMapper;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User saveUser(UserSaveDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ExistenceException(Error.USER_EXISTS_WITH_EMAIL.getMessage());
        } else if (!userDto.getPassword().equals(userDto.getRepeatPassword())) {
            throw new VerificationException(Error.PASSWORD_CONFIRMATION_IS_FAILED.getMessage());
        }

        User user = UserMapper.INSTANCE.fromUserSaveDto(userDto);
        user = userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
