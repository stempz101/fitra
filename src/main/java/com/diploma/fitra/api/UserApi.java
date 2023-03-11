package com.diploma.fitra.api;

import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.user.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/users")
public interface UserApi {

    @PostMapping("/register")
    UserDto register(@RequestBody @Validated(OnCreate.class) UserSaveDto userSaveDto);

    @PostMapping("/verify-email")
    JwtDto verifyEmail(@RequestBody @Valid EmailVerifyDto emailVerifyDto);

    @PostMapping("/authenticate")
    JwtDto authenticate(@RequestBody @Valid UserAuthDto authDto);

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    List<UserDto> getUsers(@PageableDefault(size = 15) Pageable pageable,
                           @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable Long userId);

    @PutMapping("/{userId}/info")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    UserDto updateUserInfo(@PathVariable Long userId,
                           @RequestBody @Valid UserInfoSaveDto userInfoSaveDto,
                           @AuthenticationPrincipal UserDetails userDetails);

    @PutMapping("/{userId}/email")

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    void deleteUser(@PathVariable Long userId,
                    @AuthenticationPrincipal UserDetails userDetails);
}
