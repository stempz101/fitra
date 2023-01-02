package com.diploma.fitra.api;

import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.user.UserDto;
import com.diploma.fitra.dto.user.UserSaveDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/user")
public interface UserApi {

    @PostMapping("/save")
    UserDto createUser(@RequestBody @Validated(OnCreate.class) UserSaveDto userSaveDto);

    @GetMapping
    List<UserDto> getUsers();

    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable Long userId);

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable Long userId);
}
