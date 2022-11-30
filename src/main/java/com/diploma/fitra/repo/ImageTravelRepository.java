package com.diploma.fitra.repo;

import com.diploma.fitra.model.ImageTravel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImageTravelRepository extends JpaRepository<ImageTravel, UUID> {
}
