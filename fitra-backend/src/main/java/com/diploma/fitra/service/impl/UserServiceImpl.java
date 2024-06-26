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
import com.diploma.fitra.repo.custom.UserCustomRepository;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

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
    private final UserCustomRepository userCustomRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final UserCommentRepository userCommentRepository;
    private final EmailUpdateRepository emailUpdateRepository;
    private final UsedPasswordRepository usedPasswordRepository;
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    private final UserImageRepository userImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    @Value("${frontend.link}")
    private String frontendLink;

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

        User user = UserMapper.INSTANCE.fromUserSaveDto(userSaveDto);
        user.setPassword(passwordEncoder.encode(userSaveDto.getPassword()).toCharArray());
        user.setCountry(country);
        user.setCity(city);
        user.setRole(Role.USER);
        user.setConfirmToken(UUID.randomUUID().toString());
        user.setConfirmTokenExpiration(LocalDateTime.now().plusHours(1L));
        user = userRepository.save(user);

        saveUserAvatar(user, userSaveDto.getAvatar());
        addPasswordToHistory(user);

        emailService.sendRegistrationConfirmationLink(user);

        String jwtToken = jwtService.generateToken(user);
        return JwtDto.builder().token(jwtToken).build();
    }

    @Override
    public RedirectView confirmRegistration(String token) {
        User user = userRepository.findByConfirmToken(token)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));

        if (user.isEnabled()) {
            throw new BadRequestException(Error.EMAIL_CONFIRMED.getMessage());
        } else if (LocalDateTime.now().isAfter(user.getConfirmTokenExpiration())) {
            throw new GoneException(Error.EMAIL_CONFIRMATION_EXPIRED.getMessage());
        }

        user.setEnabled(true);
        userRepository.save(user);
        return new RedirectView(frontendLink);
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
    public void resendConfirmRegistration(UserDetails userDetails) {
        log.info("Resending registration confirmation link to the user (email={})", userDetails.getUsername());

        User user = (User) userDetails;
        if (user.getConfirmTokenExpiration().isBefore(LocalDateTime.now())) {
            user.setConfirmToken(UUID.randomUUID().toString());
            user.setConfirmTokenExpiration(LocalDateTime.now().plusHours(1L));
            user = userRepository.save(user);
        }
        emailService.sendRegistrationConfirmationLink(user);
    }

    @Override
    public UserItemsResponse getUsers(String name, Long countryId, Long cityId, Pageable pageable) {
        log.info("Getting users");

        List<UserShortDto> users = userCustomRepository.findAllByQueryParams(name, countryId, cityId, pageable)
                .stream()
                .map(user -> {
                    UserShortDto userShortDto = UserMapper.INSTANCE.toUserShortDto(user);
                    userShortDto.setCountry(CountryMapper.INSTANCE.toCountryDto(user.getCountry()));
                    userShortDto.setBirthday(user.getBirthday());
                    userShortDto.setCityDto(CityMapper.INSTANCE.toCityDto(user.getCity()));
                    userShortDto.setIsAdmin(user.getRole().equals(Role.ADMIN));
                    userShortDto.setIsBlocked(user.isBlocked());
                    return userShortDto;
                })
                .collect(Collectors.toList());
        long count = userCustomRepository.countByQueryParams(name, countryId, cityId);

        return UserItemsResponse.builder()
                .items(users)
                .totalItems(count)
                .build();
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

        User user = (User) userDetails;

        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        userShortDto.setFirstName(user.getFirstName());
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            userShortDto.setIsAdmin(true);
        }
        userShortDto.setIsEnabled(userDetails.isEnabled());
        return userShortDto;
    }

    @Override
    public void createComment(Long userId, CommentSaveDto commentSaveDto, UserDetails userDetails) {
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
    }

    @Override
    public List<CommentDto> getComments(Long userId, Pageable pageable) {
        log.info("Getting comments for user (id={})", userId);

        return userCommentRepository.findAllByUserIdOrderByCreateTimeDesc(userId, pageable)
                .stream()
                .map(userComment -> {
                    CommentDto commentDto = CommentMapper.INSTANCE.toCommentDto(userComment);
                    commentDto.getUser().setIsAdmin(userComment.getUser().getRole().equals(Role.ADMIN));
                    return commentDto;
                })
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
    public RatingDto getUserRating(Long userId) {
        double rating = userCommentRepository.avgRatingByUserId(userId);
        return RatingDto.builder().rating(Double.parseDouble(String.format("%.1f", rating))).build();
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
    public void setUserIsAdmin(Long userId, Boolean newAdmin, UserDetails userDetails) {
        User admin = (User) userDetails;

        if (userId.equals(admin.getId())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (newAdmin) {
            if (user.isBlocked() || user.getRole().equals(Role.ADMIN)) {
                throw new BadRequestException(Error.USER_IS_BLOCKED_OR_ADMIN.getMessage());
            }
            user.setRole(Role.ADMIN);
        } else {
            if (user.isBlocked() || !user.getRole().equals(Role.ADMIN)) {
                throw new BadRequestException(Error.USER_IS_BLOCKED_OR_NOT_ADMIN.getMessage());
            }
            user.setRole(Role.USER);
        }
        userRepository.save(user);
    }

    @Override
    public void setUserIsBlocked(Long userId, Boolean block, UserDetails userDetails) {
        User admin = (User) userDetails;

        if (userId.equals(admin.getId())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (block) {
            if (user.isBlocked()) {
                throw new BadRequestException(Error.USER_IS_BLOCKED.getMessage());
            }
            user.setRole(Role.USER);
            user.setBlocked(true);
        } else {
            if (!user.isBlocked()) {
                throw new BadRequestException(Error.USER_IS_NOT_BLOCKED.getMessage());
            }
            user.setBlocked(false);
        }
        userRepository.save(user);
    }

    @Override
    public UserDto updateUserInfo(UserInfoSaveDto userInfoSaveDto, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Updating user (id={}) information", user.getId());

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
    public void updateUserEmail(UserEmailSaveDto userEmailSaveDto, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Updating user (id={}) email", user.getId());

        if (user.getEmail().equals(userEmailSaveDto.getEmail())) {
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
    public RedirectView confirmEmail(String token) {
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
        return new RedirectView(frontendLink);
    }

    @Override
    public void updateUserPassword(UserPasswordSaveDto userPasswordSaveDto, UserDetails userDetails) {
        User user = (User) userDetails;
        log.info("Updating user (id={}) password", user.getId());

        if (!passwordEncoder.matches(userPasswordSaveDto.getCurrentPassword(), String.valueOf(user.getPassword()))) {
            throw new UnauthorizedException(Error.UNAUTHORIZED.getMessage());
        } else if (!userPasswordSaveDto.getNewPassword().equals(userPasswordSaveDto.getRepeatPassword())) {
            throw new VerificationException(Error.PASSWORD_CONFIRMATION_IS_FAILED.getMessage());
        } else if (isPasswordPreviouslyUsed(user.getId(), userPasswordSaveDto.getNewPassword())) {
            throw new ConflictException(Error.PASSWORD_PREVIOUSLY_USED.getMessage());
        }

        user.setPassword(passwordEncoder.encode(userPasswordSaveDto.getNewPassword()).toCharArray());
        user = userRepository.save(user);

        addPasswordToHistory(user);

        log.info("User (id={}) password is updated successfully!", user.getId());
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

    private UserComment toUserComment(CommentSaveDto commentSaveDto, User user, User author) {
        UserComment comment = new UserComment();
        comment.setUser(user);
        comment.setAuthor(author);
        comment.setText(commentSaveDto.getText());
        comment.setRating(commentSaveDto.getRating());
        comment.setCreateTime(LocalDateTime.now());
        return comment;
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

    private void saveUserAvatar(User user, MultipartFile avatar) {
        if (avatar == null || avatar.isEmpty()) {
            return;
        }

        String originalFileName = avatar.getOriginalFilename();
        if (originalFileName != null) {
            String[] separatedFileName = originalFileName.split("\\.");

            String fileName = UUID.randomUUID() + "." + separatedFileName[separatedFileName.length - 1];
            Path path = Paths.get(userPhotoStoragePath, fileName);
            try {
                Files.createDirectories(path.getParent());
                try (InputStream inputStream = avatar.getInputStream()) {
                    Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                }

                UserImage userImage = new UserImage();
                userImage.setFileName(fileName);
                userImage.setAvatar(true);
                userImage.setUser(user);
                userImageRepository.save(userImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
