package com.diploma.fitra.controller;

import com.diploma.fitra.api.PlaceReviewApi;
import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.dto.comment.CommentSaveDto;
import com.diploma.fitra.dto.placereview.PlaceReviewDto;
import com.diploma.fitra.dto.placereview.PlaceReviewSaveDto;
import com.diploma.fitra.service.impl.PlaceReviewServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    public List<CommentDto> getComments(Long reviewId, Pageable pageable) {
        return placeReviewService.getComments(reviewId, pageable);
    }

    @Override
    public CommentDto createComment(CommentSaveDto commentSaveDto, Long reviewId, Authentication auth) {
        return placeReviewService.createComment(commentSaveDto, reviewId, auth);
    }

    @Override
    public List<CommentDto> getReplies(Long reviewId, Long commentId, Pageable pageable) {
        return placeReviewService.getReplies(reviewId, commentId, pageable);
    }

    @Override
    public CommentDto createReply(CommentSaveDto commentSaveDto, Long reviewId, Long commentId, Authentication auth) {
        return placeReviewService.createReply(commentSaveDto, reviewId, commentId, auth);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long reviewId, Long commentId, UserDetails userDetails) {
        placeReviewService.deleteComment(reviewId, commentId, userDetails);
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
