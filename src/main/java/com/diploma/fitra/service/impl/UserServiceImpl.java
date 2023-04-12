package com.diploma.fitra.service.impl;

import com.diploma.fitra.config.email.EmailService;
import com.diploma.fitra.config.security.JwtService;
import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.dto.comment.CommentSaveDto;
import com.diploma.fitra.dto.user.*;
import com.diploma.fitra.exception.*;
import com.diploma.fitra.mapper.*;
import com.diploma.fitra.model.*;
import com.diploma.fitra.model.enums.Role;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.*;
import com.diploma.fitra.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final UserCommentRepository userCommentRepository;
    private final UserCommentReplyRepository userCommentReplyRepository;
    private final EmailUpdateRepository emailUpdateRepository;
    private final UsedPasswordRepository usedPasswordRepository;
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Value("${photo-storage.user-photos}")
    private String userPhotoStoragePath;

    @Override
    @Transactional
    public JwtDto register(UserSaveDto userSaveDto) {
        if (userRepository.existsByEmail(userSaveDto.getEmail())) {
            throw new ExistenceException(Error.USER_EXISTS_WITH_EMAIL.getMessage());
        } else if (!userSaveDto.getPassword().equals(userSaveDto.getRepeatPassword())) {
            throw new VerificationException(Error.PASSWORD_CONFIRMATION_IS_FAILED.getMessage());
        }

        Country country = countryRepository.findById(userSaveDto.getCountryId())
                .orElseThrow(() -> new NotFoundException(Error.COUNTRY_NOT_FOUND.getMessage()));
        City city = null;
        if (userSaveDto.getCityId() != null) {
            city = cityRepository.findById(userSaveDto.getCityId())
                    .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND.getMessage()));
            if (!city.getCountry().getId().equals(country.getId())) {
                throw new BadRequestException(Error.CITY_NOT_IN_COUNTRY.getMessage());
            }
        }

        String avatarUrl = saveUserPhoto(userSaveDto);

        User user = UserMapper.INSTANCE.fromUserSaveDto(userSaveDto);
        user.setPassword(passwordEncoder.encode(userSaveDto.getPassword()).toCharArray());
        user.setCountry(country);
        user.setCity(city);
        user.setRole(Role.USER);
        user.setAvatarUrl(avatarUrl);
        user.setConfirmToken(UUID.randomUUID().toString());
        user.setConfirmTokenExpiration(LocalDateTime.now().plusHours(1L));
        user = userRepository.save(user);

        addPasswordToHistory(user);

        emailService.sendRegistrationConfirmationLink(user);

        String jwtToken = jwtService.generateToken(user);
        return JwtDto.builder().token(jwtToken).build();
    }

    @Override
    public void confirmRegistration(String token) {
        User user = userRepository.findByConfirmToken(token)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));

        if (user.isEnabled()) {
            throw new BadRequestException(Error.EMAIL_CONFIRMED.getMessage());
        } else if (LocalDateTime.now().isAfter(user.getConfirmTokenExpiration())) {
            throw new GoneException(Error.EMAIL_CONFIRMATION_EXPIRED.getMessage());
        }

        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public JwtDto authenticate(UserAuthDto authDto) {
        try {
            User user = userRepository.findByEmail(authDto.getEmail())
                    .orElseThrow(() -> new UnauthorizedException(Error.UNAUTHORIZED.getMessage()));
            if (!passwordEncoder.matches(authDto.getPassword(), String.valueOf(user.getPassword()))) {
                throw new UnauthorizedException(Error.UNAUTHORIZED.getMessage());
            }

            String jwtToken = jwtService.generateToken(user);
            return JwtDto.builder().token(jwtToken).build();
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException(Error.UNAUTHORIZED.getMessage());
        }
    }

    @Override
    public List<UserDto> getUsers(Pageable pageable, UserDetails userDetails) {
        log.info("Getting users");

        if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        return userRepository.findAll().stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long userId) {
        log.info("Getting user (id={})", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        return toUserDto(user);
    }

    @Override
    public UserShortDto getAuthorizedUser(UserDetails userDetails) {
        log.info("Getting authorized user (email={})", userDetails.getUsername());

        UserShortDto userShortDto = UserMapper.INSTANCE.toUserShortDto((User) userDetails);
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            userShortDto.setIsAdmin(true);
        }
        userShortDto.setIsEnabled(userDetails.isEnabled());
        return userShortDto;
    }

    @Override
    public CommentDto createComment(Long userId, CommentSaveDto commentSaveDto, UserDetails userDetails) {
        log.info("Commenting user (id={})", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        User author = (User) userDetails;
        if (userCommentRepository.existsByUserIdAndAuthorId(user.getId(), author.getId())) {
            throw new ConflictException(Error.USER_COMMENT_IS_EXISTS.getMessage());
        } else if (user.getId().equals(author.getId())) {
            throw new BadRequestException(Error.USER_CANT_COMMENT_HIMSELF.getMessage());
        }

        UserComment comment = userCommentRepository.save(toUserComment(commentSaveDto, user, author));

        log.info("Comment (id={}) is created to the user (id={}) by the user (id={})",
                comment.getId(), user.getId(), author.getId());
        return CommentMapper.INSTANCE.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getComments(Long userId, Pageable pageable) {
        log.info("Getting comments for user (id={})", userId);

        return userCommentRepository.findAllByUserIdOrderByCreateTimeDesc(userId, pageable)
                .stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long commentId, UserDetails userDetails) {
        log.info("Deleting the comment (id={}) in user profile", commentId);

        UserComment comment = userCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Error.USER_COMMENT_NOT_FOUND.getMessage()));

        if (!comment.getAuthor().getEmail().equals(userDetails.getUsername())) {
            if (!comment.getUser().getEmail().equals(userDetails.getUsername())) {
                if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                    throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
                }
            }
        }

        userCommentRepository.delete(comment);
        log.info("The comment (id={}) is deleted successfully from user profile", commentId);
    }

    @Override
    public CommentDto createReply(Long commentId, CommentSaveDto commentSaveDto, UserDetails userDetails) {
        log.info("Replying to the comment (id={}) for the user profile", commentId);

        UserComment comment = userCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Error.USER_COMMENT_NOT_FOUND.getMessage()));
        User author = (User) userDetails;

        UserCommentReply commentReply = userCommentReplyRepository
                .save(toUserCommentReply(commentSaveDto, author, comment));

        log.info("Reply (id={}) to the comment (id={}) for the user profile (id={}) " +
                        "is created successfully by the user (id={})", commentReply.getId(), comment.getId(),
                comment.getUser().getId(), author.getId());
        return CommentMapper.INSTANCE.toCommentDto(commentReply);
    }

    @Override
    public List<CommentDto> getReplies(Long commentId, Pageable pageable) {
        log.info("Getting replies for the comment (id={}) in user profile", commentId);

        return userCommentReplyRepository.findAllByCommentIdOrderByCreateTimeAsc(commentId)
                .stream()
                .map(CommentMapper.INSTANCE::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReply(Long replyId, UserDetails userDetails) {
        log.info("Deleting reply (id={}) of the comment from the user profile", replyId);

        UserCommentReply commentReply = userCommentReplyRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundException(Error.USER_COMMENT_REPLY_NOT_FOUND.getMessage()));
        if (!commentReply.getAuthor().getEmail().equals(userDetails.getUsername())) {
            if (!commentReply.getComment().getUser().getEmail().equals(userDetails.getUsername())) {
                if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                    throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
                }
            }
        }

        userCommentReplyRepository.delete(commentReply);

        log.info("Reply (id={}) of the comment from the user profile is deleted successfully!", replyId);
    }

    @Override
    public void sendRecoverPasswordMail(UserEmailSaveDto userEmailDto) {
        log.info("Sending recover password mail to the user (email={})", userEmailDto.getEmail());

        User user = userRepository.findByEmail(userEmailDto.getEmail())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));

        Optional<PasswordRecoveryToken> passwordRecoveryTokenOpt = passwordRecoveryTokenRepository
                .findByUserEmail(user.getEmail());
        PasswordRecoveryToken passwordRecoveryToken;
        LocalDateTime currentTime = LocalDateTime.now();
        if (passwordRecoveryTokenOpt.isPresent()) {
            passwordRecoveryToken = passwordRecoveryTokenOpt.get();
            if (!currentTime.isAfter(passwordRecoveryToken.getTokenExpiration())) {
                throw new ConflictException(Error.PASSWORD_RECOVERY_CAN_NOT_BE_SENT.getMessage());
            }
        } else {
            passwordRecoveryToken = new PasswordRecoveryToken();
            passwordRecoveryToken.setUser(user);
        }
        passwordRecoveryToken.setToken(UUID.randomUUID().toString());
        passwordRecoveryToken.setTokenExpiration(currentTime.plusHours(1L));

        passwordRecoveryToken = passwordRecoveryTokenRepository.save(passwordRecoveryToken);

        emailService.sendPasswordRecoveryLink(user, passwordRecoveryToken);
    }

    @Override
    @Transactional
    public void recoverPassword(String token, UserPasswordSaveDto userPasswordSaveDto) {
        log.info("Password recovery (token={}) is proceeding", token);

        PasswordRecoveryToken passwordRecoveryToken = passwordRecoveryTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException(Error.PASSWORD_RECOVERY_TOKEN_NOT_FOUND.getMessage()));
        User user = passwordRecoveryToken.getUser();

        if (LocalDateTime.now().isAfter(passwordRecoveryToken.getTokenExpiration())) {
            throw new GoneException(Error.PASSWORD_RECOVERY_EXPIRED.getMessage());
        } else if (!userPasswordSaveDto.getNewPassword().equals(userPasswordSaveDto.getRepeatPassword())) {
            throw new VerificationException(Error.PASSWORD_CONFIRMATION_IS_FAILED.getMessage());
        } else if (isPasswordPreviouslyUsed(user.getId(), userPasswordSaveDto.getNewPassword())) {
            throw new ConflictException(Error.PASSWORD_PREVIOUSLY_USED.getMessage());
        }

        user.setPassword(passwordEncoder.encode(userPasswordSaveDto.getNewPassword()).toCharArray());
        user = userRepository.save(user);

        addPasswordToHistory(user);

        passwordRecoveryTokenRepository.delete(passwordRecoveryToken);

        log.info("Password recovery for user (id={}) is proceeded successfully!", user.getId());
    }

    @Override
    public UserDto updateUserInfo(Long userId, UserInfoSaveDto userInfoSaveDto, UserDetails userDetails) {
        log.info("Updating user (id={}) information", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (!user.getEmail().equals(userDetails.getUsername())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        }
        Country country = countryRepository.findById(userInfoSaveDto.getCountryId())
                .orElseThrow(() -> new NotFoundException(Error.COUNTRY_NOT_FOUND.getMessage()));
        City city = null;
        if (userInfoSaveDto.getCityId() != null) {
            city = cityRepository.findById(userInfoSaveDto.getCityId())
                    .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND.getMessage()));
            if (!city.getCountry().getId().equals(country.getId())) {
                throw new BadRequestException(Error.CITY_NOT_IN_COUNTRY.getMessage());
            }
        }

        user = UpdateMapper.updateUserWithPresentUserInfoSaveDtoFields(user, userInfoSaveDto);
        user.setCountry(country);
        user.setCity(city);
        user = userRepository.save(user);

        log.info("User (id={}) information is updated successfully!", user.getId());
        return toUserDto(user);
    }

    @Override
    public void updateUserEmail(Long userId, UserEmailSaveDto userEmailSaveDto, UserDetails userDetails) {
        log.info("Updating user (id={}) email", userId);

        User user = (User) userDetails;
        if (!userId.equals(user.getId())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        } else if (user.getEmail().equals(userEmailSaveDto.getEmail())) {
            throw new BadRequestException(Error.EMAIL_IS_CURRENT.getMessage());
        } else if (userRepository.existsByEmail(userEmailSaveDto.getEmail())) {
            throw new BadRequestException(Error.USER_EXISTS_WITH_EMAIL.getMessage());
        }

        Optional<EmailUpdate> emailUpdateOpt = emailUpdateRepository.findByUser(user);
        EmailUpdate emailUpdate;
        LocalDateTime currentTime = LocalDateTime.now();
        if (emailUpdateOpt.isPresent()) {
            emailUpdate = emailUpdateOpt.get();
            if (!currentTime.isAfter(emailUpdate.getConfirmTokenExpiration())) {
                throw new ConflictException(Error.EMAIL_CAN_NOT_BE_UPDATED.getMessage());
            }
        } else {
            emailUpdate = new EmailUpdate();
            emailUpdate.setUser(user);
        }
        emailUpdate.setEmail(userEmailSaveDto.getEmail());
        emailUpdate.setConfirmToken(UUID.randomUUID().toString());
        emailUpdate.setConfirmTokenExpiration(currentTime.plusHours(1L));

        emailUpdate = emailUpdateRepository.save(emailUpdate);

        emailService.sendNewEmailConfirmationLink(user, emailUpdate);

        log.info("User (id={}) email verification is sent to the new specified email", user.getId());
    }

    @Override
    @Transactional
    public void confirmEmail(String token) {
        log.info("Confirming new email");

        EmailUpdate emailUpdate = emailUpdateRepository.findByConfirmToken(token)
                .orElseThrow(() -> new NotFoundException(Error.EMAIL_UPDATE_NOT_FOUND.getMessage()));

        if (LocalDateTime.now().isAfter(emailUpdate.getConfirmTokenExpiration())) {
            throw new GoneException(Error.EMAIL_CONFIRMATION_EXPIRED.getMessage());
        }

        User user = emailUpdate.getUser();
        user.setEmail(emailUpdate.getEmail());
        userRepository.save(user);

        emailUpdateRepository.delete(emailUpdate);

        log.info("User (id={}) email is updated successfully!", user.getId());
    }

    @Override
    public void updateUserPassword(Long userId, UserPasswordSaveDto userPasswordSaveDto, UserDetails userDetails) {
        log.info("Updating user (id={}) password", userId);

        User user = (User) userDetails;
        if (!userId.equals(user.getId())) {
            if (!userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        } else if (!passwordEncoder.matches(userPasswordSaveDto.getCurrentPassword(), String.valueOf(user.getPassword()))) {
            throw new UnauthorizedException(Error.UNAUTHORIZED.getMessage());
        } else if (!userPasswordSaveDto.getNewPassword().equals(userPasswordSaveDto.getRepeatPassword())) {
            throw new VerificationException(Error.PASSWORD_CONFIRMATION_IS_FAILED.getMessage());
        } else if (isPasswordPreviouslyUsed(user.getId(), userPasswordSaveDto.getNewPassword())) {
            throw new ConflictException(Error.PASSWORD_PREVIOUSLY_USED.getMessage());
        }

        user.setPassword(passwordEncoder.encode(userPasswordSaveDto.getNewPassword()).toCharArray());
        user = userRepository.save(user);

        addPasswordToHistory(user);

        log.info("User (id={}) password is updated successfully!", userId);
    }

    @Override
    public void deleteUser(Long userId, UserDetails userDetails) {
        log.info("Deleting user (id={})", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (user.getEmail().equals(userDetails.getUsername())) {
            throw new BadRequestException(Error.ADMIN_CANT_DELETE_HIMSELF.getMessage());
        }

        userRepository.delete(user);
        log.info("User (id={}) is deleted successfully!", user.getId());
    }

    private UserDto toUserDto(User user) {
        UserDto userDto = UserMapper.INSTANCE.toUserDto(user);
        userDto.setCountry(CountryMapper.INSTANCE.toCountryDto(user.getCountry()));
        userDto.setCity(CityMapper.INSTANCE.toCityDto(user.getCity()));
        userDto.setRating(userCommentRepository.avgRatingByUserId(user.getId()));
        userDto.setCommentCount(userCommentRepository.countByUserId(user.getId()));
        if (user.getRole().equals(Role.ADMIN)) {
            userDto.setIsAdmin(true);
        }
        return userDto;
    }

    private CommentDto toCommentDto(UserComment comment) {
        CommentDto commentDto = CommentMapper.INSTANCE.toCommentDto(comment);
        commentDto.setReplies(userCommentReplyRepository.countByCommentId(comment.getId()));

        return commentDto;
    }

    private UserComment toUserComment(CommentSaveDto commentSaveDto, User user, User author) {
        UserComment comment = new UserComment();
        comment.setUser(user);
        comment.setAuthor(author);
        comment.setText(commentSaveDto.getText());
        comment.setRating(commentSaveDto.getRating());
        comment.setCreateTime(LocalDateTime.now());
        return comment;
    }

    private UserCommentReply toUserCommentReply(CommentSaveDto commentSaveDto, User author, UserComment comment) {
        UserCommentReply commentReply = new UserCommentReply();
        commentReply.setComment(comment);
        commentReply.setAuthor(author);
        commentReply.setText(commentSaveDto.getText());
        commentReply.setCreateTime(LocalDateTime.now());
        return commentReply;
    }

    private void addPasswordToHistory(User user) {
        UsedPassword usedPassword = new UsedPassword();
        usedPassword.setUser(user);
        usedPassword.setPassword(user.getPassword().toCharArray());
        usedPasswordRepository.save(usedPassword);
    }

    private boolean isPasswordPreviouslyUsed(Long userId, String newPassword) {
        return usedPasswordRepository.findAllByUserId(userId).stream()
                .anyMatch(usedPassword -> passwordEncoder.matches(newPassword, String.valueOf(usedPassword.getPassword())));
    }

    private String saveUserPhoto(UserSaveDto userSaveDto) {
        if (userSaveDto.getAvatar() == null || userSaveDto.getAvatar().isEmpty()) {
            return null;
        }

        System.out.println(userSaveDto.getAvatar().getOriginalFilename());
        String originalFileName = userSaveDto.getAvatar().getOriginalFilename();
        String[] separatedFileName = originalFileName.split("\\.");
        String fileName = UUID.randomUUID() + "." + separatedFileName[separatedFileName.length - 1];
        Path path = Paths.get(userPhotoStoragePath, fileName);
        try {
            Files.createDirectories(path.getParent());
            try (InputStream inputStream = userSaveDto.getAvatar().getInputStream()) {
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            }
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
