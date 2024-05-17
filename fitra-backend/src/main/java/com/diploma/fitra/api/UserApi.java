package com.diploma.fitra.api;

import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.dto.comment.CommentSaveDto;
import com.diploma.fitra.dto.group.IsNotUserComment;
import com.diploma.fitra.dto.group.IsUserComment;
import com.diploma.fitra.dto.group.OnRecover;
import com.diploma.fitra.dto.group.OnUpdate;
import com.diploma.fitra.dto.user.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RequestMapping("/api/v1/users")
public interface UserApi {

    @PostMapping("/register")
    JwtDto register(@ModelAttribute @Valid UserSaveDto userSaveDto);

    @GetMapping("/confirm-registration")
    RedirectView confirmRegistration(@RequestParam String token);

    @PostMapping("/authenticate")
    JwtDto authenticate(@RequestBody @Valid UserAuthDto authDto);

    @GetMapping("/enabled")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    boolean isUserEnabled(@AuthenticationPrincipal UserDetails userDetails);

    @PostMapping("/resend-confirm-registration")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> resendConfirmRegistration(@AuthenticationPrincipal UserDetails userDetails);

    @GetMapping
    UserItemsResponse getUsers(@RequestParam(required = false) String name,
                           @RequestParam(required = false) Long countryId,
                           @RequestParam(required = false) Long cityId,
                           @PageableDefault(size = 15) Pageable pageable);

    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable Long userId);

    @GetMapping("/authorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    UserShortDto getAuthorizedUser(@AuthenticationPrincipal UserDetails userDetails);

    @PostMapping("/{userId}/comments")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> createComment(@PathVariable Long userId,
                             @RequestBody @Validated(IsUserComment.class) CommentSaveDto commentSaveDto,
                             @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/{userId}/comments")
    List<CommentDto> getComments(@PathVariable Long userId,
                                 @PageableDefault Pageable pageable);

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                       @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/{userId}/rating")
    RatingDto getUserRating(@PathVariable Long userId);

    @PostMapping("/send-recover-password-mail")
    ResponseEntity<Void> sendRecoverPasswordMail(@RequestBody @Valid UserEmailSaveDto userEmailDto);

    @PostMapping("/recover-password")
    ResponseEntity<Void> recoverPassword(@RequestParam String token,
                                         @RequestBody @Validated(OnRecover.class) UserPasswordSaveDto userPasswordSaveDto);

    @PutMapping("/{userId}/admin/{newAdmin}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> setUserIsAdmin(@PathVariable Long userId,
                                        @PathVariable Boolean newAdmin,
                                        @AuthenticationPrincipal UserDetails userDetails);

    @PutMapping("/{userId}/block/{block}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> setUserIsBlocked(@PathVariable Long userId,
                                          @PathVariable Boolean block,
                                          @AuthenticationPrincipal UserDetails userDetails);

    @PutMapping("/info")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    UserDto updateUserInfo(@RequestBody @Valid UserInfoSaveDto userInfoSaveDto,
                           @AuthenticationPrincipal UserDetails userDetails);

    @PutMapping("/email")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> updateUserEmail(@RequestBody @Valid UserEmailSaveDto userEmailSaveDto,
                                         @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/confirm-email")
    RedirectView confirmEmail(@RequestParam String token);

    @PutMapping("/password")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> updateUserPassword(@RequestBody @Validated(OnUpdate.class) UserPasswordSaveDto userPasswordSaveDto,
                                            @AuthenticationPrincipal UserDetails userDetails);

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    void deleteUser(@PathVariable Long userId,
                    @AuthenticationPrincipal UserDetails userDetails);
}
