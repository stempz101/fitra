package com.diploma.fitra.repo;

import com.diploma.fitra.model.User;
import com.diploma.fitra.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Long> {

    UserImage findByUserAndAvatarIsTrue(User user);

    Optional<UserImage> findByFileNameAndAvatarIsTrue(String filename);
}
