package com.diploma.fitra.repo;

import com.diploma.fitra.model.ImageTravel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageTravelRepository extends JpaRepository<ImageTravel, UUID> {
}
