package com.diploma.fitra.repo;

import com.diploma.fitra.model.ImageUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageUserRepository extends JpaRepository<ImageUser, UUID> {
}
