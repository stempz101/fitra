package com.diploma.fitra.controller;

import com.diploma.fitra.api.PlaceReviewApi;
import com.diploma.fitra.dto.placereview.PlaceReviewDto;
import com.diploma.fitra.dto.placereview.PlaceReviewSaveDto;
import com.diploma.fitra.service.impl.PlaceReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceReviewController implements PlaceReviewApi {

    private final PlaceReviewServiceImpl placeReviewService;

    @Override
    public PlaceReviewDto createPlaceReview(PlaceReviewSaveDto placeReviewSaveDto, Authentication auth) {
        return placeReviewService.createPlaceReview(placeReviewSaveDto, auth);
    }

    @Override
    public List<PlaceReviewDto> getPlaceReviews(Pageable pageable, Authentication auth) {
        return placeReviewService.getPlaceReviews(pageable, auth);
    }

    @Override
    public PlaceReviewDto getPlaceReview(Long reviewId, Authentication auth) {
        return placeReviewService.getPlaceReview(reviewId, auth);
    }

    @Override
    public ResponseEntity<Void> setLike(Long reviewId, Authentication auth) {
        placeReviewService.setLike(reviewId, auth);
        return ResponseEntity.noContent().build();
    }

    @Override
    public PlaceReviewDto updatePlaceReview(PlaceReviewSaveDto placeReviewSaveDto, Long reviewId, Authentication auth) {
        return placeReviewService.updatePlaceReview(placeReviewSaveDto, reviewId, auth);
    }

    @Override
    public ResponseEntity<Void> deletePlaceReview(Long reviewId, Authentication auth) {
        placeReviewService.deletePlaceReview(reviewId, auth);
        return ResponseEntity.noContent().build();
    }
}
