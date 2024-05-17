package com.diploma.fitra.service;

import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.dto.comment.CommentSaveDto;
import com.diploma.fitra.dto.user.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

public interface UserService {

    JwtDto register(UserSaveDto userSaveDto);

    RedirectView confirmRegistration(String token);

    JwtDto authenticate(UserAuthDto authDto);

    void resendConfirmRegistration(UserDetails userDetails);

    UserItemsResponse getUsers(String name, Long countryId, Long cityId, Pageable pageable);

    UserDto getUser(Long userId);

    UserShortDto getAuthorizedUser(UserDetails userDetails);

    void createComment(Long userId, CommentSaveDto commentSaveDto, UserDetails userDetails);

    List<CommentDto> getComments(Long userId, Pageable pageable);

    void deleteComment(Long commentId, UserDetails userDetails);

    RatingDto getUserRating(Long userId);

    void sendRecoverPasswordMail(UserEmailSaveDto userEmailDto);

    void recoverPassword(String token, UserPasswordSaveDto userPasswordSaveDto);

    void setUserIsAdmin(Long userId, Boolean newAdmin, UserDetails userDetails);

    void setUserIsBlocked(Long userId, Boolean block, UserDetails userDetails);

    UserDto updateUserInfo(UserInfoSaveDto userInfoSaveDto, UserDetails userDetails);

    void updateUserEmail(UserEmailSaveDto userEmailSaveDto, UserDetails userDetails);

    RedirectView confirmEmail(String token);

    void updateUserPassword(UserPasswordSaveDto userPasswordSaveDto, UserDetails userDetails);

    void deleteUser(Long userId, UserDetails userDetails);
}
