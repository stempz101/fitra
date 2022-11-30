package com.diploma.fitra.repo;

import com.diploma.fitra.model.ImageUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageUserRepository extends JpaRepository<ImageUser, UUID> {
}
