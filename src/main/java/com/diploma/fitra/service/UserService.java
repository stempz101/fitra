package com.diploma.fitra.service;

import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.dto.comment.CommentSaveDto;
import com.diploma.fitra.dto.user.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    UserDto register(UserSaveDto userSaveDto);

    JwtDto confirmRegistration(String token);

    JwtDto authenticate(UserAuthDto authDto);

    List<UserDto> getUsers(Pageable pageable, UserDetails userDetails);

    UserDto getUser(Long userId);

    CommentDto createComment(Long userId, CommentSaveDto commentSaveDto, UserDetails userDetails);

    List<CommentDto> getComments(Long userId, Pageable pageable);

    void deleteComment(Long commentId, UserDetails userDetails);

    CommentDto createReply(Long commentId, CommentSaveDto commentSaveDto, UserDetails userDetails);

    List<CommentDto> getReplies(Long commentId, Pageable pageable);

    void deleteReply(Long replyId, UserDetails userDetails);

    void sendRecoverPasswordMail(UserEmailSaveDto userEmailDto);

    void recoverPassword(String token, UserPasswordSaveDto userPasswordSaveDto);

    UserDto updateUserInfo(Long userId, UserInfoSaveDto userInfoSaveDto, UserDetails userDetails);

    void updateUserEmail(Long userId, UserEmailSaveDto userEmailSaveDto, UserDetails userDetails);

    void confirmEmail(String token);

    void updateUserPassword(Long userId, UserPasswordSaveDto userPasswordSaveDto, UserDetails userDetails);

    void deleteUser(Long userId, UserDetails userDetails);
}
