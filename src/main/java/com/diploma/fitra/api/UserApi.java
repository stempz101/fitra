package com.diploma.fitra.api;

import com.diploma.fitra.dto.user.UserDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/users")
public interface UserApi {

    @GetMapping
    List<UserDto> getUsers();

    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable Long userId);

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    void deleteUser(@PathVariable Long userId);
}
