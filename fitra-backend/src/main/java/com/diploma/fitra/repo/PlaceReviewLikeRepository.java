package com.diploma.fitra.repo;

import com.diploma.fitra.model.PlaceReview;
import com.diploma.fitra.model.PlaceReviewLike;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.key.PlaceReviewLikeKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceReviewLikeRepository extends JpaRepository<PlaceReviewLike, PlaceReviewLikeKey> {

    long countByPlaceReview(PlaceReview placeReview);

    boolean existsByPlaceReviewAndUser(PlaceReview placeReview, User user);
}
