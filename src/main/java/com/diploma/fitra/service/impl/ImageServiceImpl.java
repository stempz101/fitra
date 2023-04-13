package com.diploma.fitra.service.impl;

import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.model.UserPhoto;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.UserPhotoRepository;
import com.diploma.fitra.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final UserPhotoRepository userPhotoRepository;

    @Value("${photo-storage.user-photos}")
    private String userImagePath;

    @Override
    public byte[] getUserAvatar(String fileName) {
        log.info("Getting user avatar (fileName={})", fileName);

        UserPhoto avatar = userPhotoRepository.findByFileNameAndAvatarIsTrue(fileName)
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
}
