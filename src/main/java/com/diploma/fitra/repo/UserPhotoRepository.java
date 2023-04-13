package com.diploma.fitra.repo;

import com.diploma.fitra.model.User;
import com.diploma.fitra.model.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

    UserPhoto findByUserAndAvatarIsTrue(User user);

    Optional<UserPhoto> findByFileNameAndAvatarIsTrue(String filename);
}
