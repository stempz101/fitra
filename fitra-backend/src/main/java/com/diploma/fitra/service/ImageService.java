package com.diploma.fitra.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    byte[] getUserAvatar(Long userId);

    void setUserAvatar(Long userId, String fileName, UserDetails userDetails);

    byte[] getUserPhoto(Long userId, String fileName);

    List<String> getAllUserPhotos(Long userId);

    void uploadUserPhoto(Long userId, MultipartFile photo, UserDetails userDetails);

    void deleteUserPhoto(Long userId, String fileName, UserDetails userDetails);

    void uploadTravelPhoto(Long travelId, MultipartFile photo, UserDetails userDetails);

    byte[] getTravelPhoto(Long travelId, String fileName);

    byte[] getTravelMainPhoto(Long travelId);

    void setTravelMainPhoto(Long travelId, String fileName, UserDetails userDetails);

    List<String> getAllTravelPhotos(Long travelId);

    void deleteTravelPhoto(Long travelId, String fileName, UserDetails userDetails);
}
