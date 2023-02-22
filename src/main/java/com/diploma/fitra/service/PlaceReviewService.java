package com.diploma.fitra.service;

import com.diploma.fitra.dto.placereview.PlaceReviewDto;
import com.diploma.fitra.dto.placereview.PlaceReviewSaveDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PlaceReviewService {

    PlaceReviewDto createPlaceReview(PlaceReviewSaveDto placeReviewSaveDto, Authentication auth);

    List<PlaceReviewDto> getPlaceReviews(Pageable pageable);

    PlaceReviewDto getPlaceReview(Long reviewId);

    void setLike(Long reviewId, Authentication auth);

    void setDislike(Long reviewId, Authentication auth);

    PlaceReviewDto updatePlaceReview(PlaceReviewSaveDto placeReviewSaveDto, Long reviewId, Authentication auth);

    void deletePlaceReview(Long reviewId, Authentication auth);
}
