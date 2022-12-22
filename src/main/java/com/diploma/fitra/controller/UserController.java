package com.diploma.fitra.controller;

import com.diploma.fitra.dto.user.UserSaveDto;
import com.diploma.fitra.model.User;
import com.diploma.fitra.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/save")
    public User saveUser(@RequestBody @Valid UserSaveDto userDto) {
        return userService.saveUser(userDto);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
