package com.diploma.fitra.controller;

import com.diploma.fitra.api.UserApi;
import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.dto.comment.CommentSaveDto;
import com.diploma.fitra.dto.user.*;
import com.diploma.fitra.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public JwtDto register(UserSaveDto userSaveDto) {
        return userService.register(userSaveDto);
    }

    @Override
    public RedirectView confirmRegistration(String token) {
        return userService.confirmRegistration(token);
    }

    @Override
    public JwtDto authenticate(UserAuthDto authDto) {
        return userService.authenticate(authDto);
    }

    @Override
    public boolean isUserEnabled(UserDetails userDetails) {
        return userDetails.isEnabled();
    }

    @Override
    public ResponseEntity<Void> resendConfirmRegistration(UserDetails userDetails) {
        userService.resendConfirmRegistration(userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<UserDto> getUsers(Pageable pageable, UserDetails userDetails) {
        return userService.getUsers(pageable, userDetails);
    }

    @Override
    public UserDto getUser(Long userId) {
        return userService.getUser(userId);
    }

    @Override
    public UserShortDto getAuthorizedUser(UserDetails userDetails) {
        return userService.getAuthorizedUser(userDetails);
    }

    @Override
    public CommentDto createComment(Long userId, CommentSaveDto commentSaveDto, UserDetails userDetails) {
        return userService.createComment(userId, commentSaveDto, userDetails);
    }

    @Override
    public List<CommentDto> getComments(Long userId, Pageable pageable) {
        return userService.getComments(userId, pageable);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long commentId, UserDetails userDetails) {
        userService.deleteComment(commentId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public CommentDto createReply(Long commentId, CommentSaveDto commentSaveDto, UserDetails userDetails) {
        return userService.createReply(commentId, commentSaveDto, userDetails);
    }

    @Override
    public List<CommentDto> getReplies(Long commentId, Pageable pageable) {
        return userService.getReplies(commentId, pageable);
    }

    @Override
    public ResponseEntity<Void> deleteReply(Long replyId, UserDetails userDetails) {
        userService.deleteReply(replyId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> sendRecoverPasswordMail(UserEmailSaveDto userEmailDto) {
        userService.sendRecoverPasswordMail(userEmailDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> recoverPassword(String token, UserPasswordSaveDto userPasswordSaveDto) {
        userService.recoverPassword(token, userPasswordSaveDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    public UserDto updateUserInfo(Long userId, UserInfoSaveDto userInfoSaveDto, UserDetails userDetails) {
        return userService.updateUserInfo(userId, userInfoSaveDto, userDetails);
    }

    @Override
    public ResponseEntity<Void> updateUserEmail(Long userId, UserEmailSaveDto userEmailSaveDto, UserDetails userDetails) {
        userService.updateUserEmail(userId, userEmailSaveDto, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public RedirectView confirmEmail(String token) {
        return userService.confirmEmail(token);
    }

    @Override
    public ResponseEntity<Void> updateUserPassword(Long userId, UserPasswordSaveDto userPasswordSaveDto, UserDetails userDetails) {
        userService.updateUserPassword(userId, userPasswordSaveDto, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public void deleteUser(Long userId, UserDetails userDetails) {
        userService.deleteUser(userId, userDetails);
    }
}
