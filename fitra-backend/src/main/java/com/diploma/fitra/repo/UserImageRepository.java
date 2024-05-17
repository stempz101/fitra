package com.diploma.fitra.repo;

import com.diploma.fitra.model.User;
import com.diploma.fitra.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    List<UserImage> findAllByUserIdOrderById(Long userId);

    Optional<UserImage> findByUserIdAndFileName(Long userId, String fileName);

    Optional<UserImage> findByUserIdAndAvatarIsTrue(Long userId);

    Optional<UserImage> findFirstByUserIdAndAvatarIsFalse(Long userId);
}
