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

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
