package com.diploma.fitra.controller;

import com.diploma.fitra.api.PlaceReviewApi;
import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.dto.comment.CommentSaveDto;
import com.diploma.fitra.dto.placereview.PlaceReviewDto;
import com.diploma.fitra.dto.placereview.PlaceReviewSaveDto;
import com.diploma.fitra.service.PlaceReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaceReviewController implements PlaceReviewApi {

    private final PlaceReviewService placeReviewService;

    @Override
    public PlaceReviewDto createPlaceReview(PlaceReviewSaveDto placeReviewSaveDto, UserDetails userDetails) {
        return placeReviewService.createPlaceReview(placeReviewSaveDto, userDetails);
    }

    @Override
    public List<PlaceReviewDto> getPlaceReviews(Pageable pageable, UserDetails userDetails) {
        return placeReviewService.getPlaceReviews(pageable, userDetails);
    }

    @Override
    public PlaceReviewDto getPlaceReview(Long reviewId, UserDetails userDetails) {
        return placeReviewService.getPlaceReview(reviewId, userDetails);
    }

    @Override
    public ResponseEntity<Void> setLike(Long reviewId, UserDetails userDetails) {
        placeReviewService.setLike(reviewId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public List<CommentDto> getComments(Long reviewId, Pageable pageable) {
        return placeReviewService.getComments(reviewId, pageable);
    }

    @Override
    public CommentDto createComment(CommentSaveDto commentSaveDto, Long reviewId, UserDetails userDetails) {
        return placeReviewService.createComment(commentSaveDto, reviewId, userDetails);
    }

    @Override
    public List<CommentDto> getReplies(Long reviewId, Long commentId, Pageable pageable) {
        return placeReviewService.getReplies(reviewId, commentId, pageable);
    }

    @Override
    public CommentDto createReply(CommentSaveDto commentSaveDto, Long reviewId, Long commentId, UserDetails userDetails) {
        return placeReviewService.createReply(commentSaveDto, reviewId, commentId, userDetails);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long reviewId, Long commentId, UserDetails userDetails) {
        placeReviewService.deleteComment(reviewId, commentId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @Override
    public PlaceReviewDto updatePlaceReview(PlaceReviewSaveDto placeReviewSaveDto, Long reviewId, UserDetails userDetails) {
        return placeReviewService.updatePlaceReview(placeReviewSaveDto, reviewId, userDetails);
    }

    @Override
    public ResponseEntity<Void> deletePlaceReview(Long reviewId, UserDetails userDetails) {
        placeReviewService.deletePlaceReview(reviewId, userDetails);
        return ResponseEntity.noContent().build();
    }
}
