package com.diploma.fitra.repo;

import com.diploma.fitra.model.TravelImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelImageRepository extends JpaRepository<TravelImage, Long> {

    List<TravelImage> findAllByTravelIdOrderById(Long travelId);

    Optional<TravelImage> findFirstByTravelIdAndMainIsFalse(Long travelId);

    Optional<TravelImage> findByTravelIdAndFileName(Long travelId, String fileName);

    Optional<TravelImage> findByTravelIdAndMainIsTrue(Long travelId);
}
