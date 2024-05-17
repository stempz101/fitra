package com.diploma.fitra.service.impl;

import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ConflictException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.TravelImage;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.UserImage;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.TravelImageRepository;
import com.diploma.fitra.repo.TravelRepository;
import com.diploma.fitra.repo.UserImageRepository;
import com.diploma.fitra.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final UserImageRepository userImageRepository;
    private final TravelImageRepository travelImageRepository;
    private final TravelRepository travelRepository;

    @Value("${photo-storage.user-photos}")
    private String userImagePath;

    @Value("${photo-storage.travel-photos}")
    private String travelImagePath;

    @Override
    public byte[] getUserAvatar(Long userId) {
        log.info("Getting user (id={}) avatar", userId);

        UserImage avatar = userImageRepository.findByUserIdAndAvatarIsTrue(userId)
                .orElseThrow(() -> new NotFoundException(Error.IMAGE_NOT_FOUND.getMessage()));

        File file = new File(userImagePath + avatar.getFileName());
        byte[] fileBytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
            return fileBytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setUserAvatar(Long userId, String fileName, UserDetails userDetails) {
        User user = (User) userDetails;
        UserImage newAvatar = userImageRepository.findByUserIdAndFileName(user.getId(), fileName)
                .orElseThrow(() -> new NotFoundException(Error.IMAGE_NOT_FOUND.getMessage()));
        if (!newAvatar.isAvatar()) {
            UserImage avatar = userImageRepository.findByUserIdAndAvatarIsTrue(user.getId())
                    .orElseThrow(() -> new NotFoundException(Error.IMAGE_NOT_FOUND.getMessage()));
            avatar.setAvatar(false);
            newAvatar.setAvatar(true);

            userImageRepository.save(avatar);
            userImageRepository.save(newAvatar);
        }
    }

    @Override
    public byte[] getUserPhoto(Long userId, String fileName) {
        log.info("Getting user (id={}) photo (fileName={})", userId, fileName);

        UserImage photo = userImageRepository.findByUserIdAndFileName(userId, fileName)
                .orElseThrow(() -> new NotFoundException(Error.IMAGE_NOT_FOUND.getMessage()));

        File file = new File(userImagePath + photo.getFileName());
        byte[] fileBytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
            return fileBytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getAllUserPhotos(Long userId) {
        log.info("Getting all user (id={}) photo filenames", userId);

        return userImageRepository.findAllByUserIdOrderById(userId)
                .stream()
                .map(UserImage::getFileName)
                .collect(Collectors.toList());
    }

    @Override
    public void uploadUserPhoto(Long userId, MultipartFile photo, UserDetails userDetails) {
        User user = (User) userDetails;
        if (photo == null || photo.isEmpty()) {
            throw new BadRequestException(Error.PHOTO_IS_NULL_OR_EMPTY.getMessage());
        }

        String fileName = saveUserPhotoLocally(photo);
        UserImage userImage = new UserImage();
        userImage.setFileName(fileName);
        userImage.setUser(user);
        userImageRepository.save(userImage);
    }

    @Override
    public void deleteUserPhoto(Long userId, String fileName, UserDetails userDetails) {
        User user = (User) userDetails;
        UserImage photo = userImageRepository.findByUserIdAndFileName(user.getId(), fileName)
                .orElseThrow(() -> new NotFoundException(Error.IMAGE_NOT_FOUND.getMessage()));
        if (photo.isAvatar()) {
            Optional<UserImage> optNewAvatar = userImageRepository.findFirstByUserIdAndAvatarIsFalse(userId);
            if (optNewAvatar.isPresent()) {
                UserImage newAvatar = optNewAvatar.get();
                newAvatar.setAvatar(true);
                userImageRepository.save(newAvatar);
            }
        }
        deleteUserImageLocally(photo.getFileName());
        userImageRepository.delete(photo);
    }

    @Override
    public void uploadTravelPhoto(Long travelId, MultipartFile photo, UserDetails userDetails) {
        log.info("Uploading travel (id={}) photo", travelId);

        User user = (User) userDetails;
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getId().equals(user.getId())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (photo == null || photo.isEmpty()) {
            throw new BadRequestException(Error.PHOTO_IS_NULL_OR_EMPTY.getMessage());
        }

        String fileName = saveTravelPhotoLocally(photo);
        TravelImage travelImage = new TravelImage();
        travelImage.setFileName(fileName);
        travelImage.setTravel(travel);
        travelImageRepository.save(travelImage);
    }

    @Override
    public byte[] getTravelPhoto(Long travelId, String fileName) {
        log.info("Getting travel (id={}) photo (fileName={})", travelId, fileName);

        TravelImage photo = travelImageRepository.findByTravelIdAndFileName(travelId, fileName)
                .orElseThrow(() -> new NotFoundException(Error.IMAGE_NOT_FOUND.getMessage()));

        File file = new File(travelImagePath + photo.getFileName());
        byte[] fileBytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
            return fileBytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getTravelMainPhoto(Long travelId) {
        log.info("Getting travel (id={}) main photo", travelId);

        TravelImage photo = travelImageRepository.findByTravelIdAndMainIsTrue(travelId)
                .orElseThrow(() -> new NotFoundException(Error.IMAGE_NOT_FOUND.getMessage()));

        File file = new File(travelImagePath + photo.getFileName());
        byte[] fileBytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
            return fileBytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void setTravelMainPhoto(Long travelId, String fileName, UserDetails userDetails) {
        log.info("Setting new travel (id={}) main photo (fileName={})", travelId, fileName);

        User user = (User) userDetails;
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getId().equals(user.getId())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        TravelImage newMainPhoto = travelImageRepository.findByTravelIdAndFileName(travel.getId(), fileName)
                .orElseThrow(() -> new NotFoundException(Error.IMAGE_NOT_FOUND.getMessage()));
        if (!newMainPhoto.isMain()) {
            TravelImage mainPhoto = travelImageRepository.findByTravelIdAndMainIsTrue(travel.getId())
                    .orElseThrow(() -> new NotFoundException(Error.IMAGE_NOT_FOUND.getMessage()));
            mainPhoto.setMain(false);
            newMainPhoto.setMain(true);

            travelImageRepository.save(mainPhoto);
            travelImageRepository.save(newMainPhoto);
        }
    }

    @Override
    public List<String> getAllTravelPhotos(Long travelId) {
        log.info("Getting all travel (id={}) photo filenames", travelId);

        return travelImageRepository.findAllByTravelIdOrderById(travelId)
                .stream()
                .map(TravelImage::getFileName)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTravelPhoto(Long travelId, String fileName, UserDetails userDetails) {
        log.info("Deleting travel (id={}) photo (fileName={})", travelId, fileName);

        User user = (User) userDetails;
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new NotFoundException(Error.TRAVEL_NOT_FOUND.getMessage()));
        if (!travel.getCreator().getId().equals(user.getId())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        TravelImage photo = travelImageRepository.findByTravelIdAndFileName(travel.getId(), fileName)
                .orElseThrow(() -> new NotFoundException(Error.IMAGE_NOT_FOUND.getMessage()));
        if (photo.isMain()) {
            TravelImage newMainPhoto = travelImageRepository.findFirstByTravelIdAndMainIsFalse(travelId)
                    .orElseThrow(() -> new ConflictException(Error.ONLY_ONE_IMAGE.getMessage()));
            newMainPhoto.setMain(true);
            travelImageRepository.save(newMainPhoto);
        }
        deleteTravelImageLocally(photo.getFileName());
        travelImageRepository.delete(photo);
    }

    private String saveUserPhotoLocally(MultipartFile photo) {
        String originalFileName = photo.getOriginalFilename();
        if (originalFileName != null) {
            String[] separatedFileName = originalFileName.split("\\.");

            String fileName = UUID.randomUUID() + "." + separatedFileName[separatedFileName.length - 1];
            Path path = Paths.get(userImagePath, fileName);
            try {
                Files.createDirectories(path.getParent());
                try (InputStream inputStream = photo.getInputStream()) {
                    Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                }
                return fileName;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private String saveTravelPhotoLocally(MultipartFile photo) {
        String originalFileName = photo.getOriginalFilename();
        if (originalFileName != null) {
            String[] separatedFileName = originalFileName.split("\\.");

            String fileName = UUID.randomUUID() + "." + separatedFileName[separatedFileName.length - 1];
            Path path = Paths.get(travelImagePath, fileName);
            try {
                Files.createDirectories(path.getParent());
                try (InputStream inputStream = photo.getInputStream()) {
                    Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                }
                return fileName;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private void deleteUserImageLocally(String fileName) {
        File file = new File(userImagePath + fileName);

        if (file.delete()) {
            log.info("User image is deleted successfully: {}", fileName);
        } else {
            throw new RuntimeException(Error.USER_IMAGE_IS_NOT_DELETED.getMessage() + ": " + fileName);
        }
    }

    private void deleteTravelImageLocally(String fileName) {
        File file = new File(travelImagePath + fileName);

        if (file.delete()) {
            log.info("Travel image is deleted successfully: {}", fileName);
        } else {
            throw new RuntimeException(Error.TRAVEL_IMAGE_IS_NOT_DELETED.getMessage() + ": " + fileName);
        }
    }
}
