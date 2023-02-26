package com.diploma.fitra.service;

import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.dto.comment.CommentSaveDto;
import com.diploma.fitra.dto.placereview.PlaceReviewDto;
import com.diploma.fitra.dto.placereview.PlaceReviewSaveDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PlaceReviewService {

    PlaceReviewDto createPlaceReview(PlaceReviewSaveDto placeReviewSaveDto, Authentication auth);

    List<PlaceReviewDto> getPlaceReviews(Pageable pageable, Authentication auth);

    PlaceReviewDto getPlaceReview(Long reviewId, Authentication auth);

    void setLike(Long reviewId, Authentication auth);

    CommentDto createComment(CommentSaveDto commentSaveDto, Long reviewId, Authentication auth);

    List<CommentDto> getComments(Long reviewId, Pageable pageable);

    CommentDto createReply(CommentSaveDto commentSaveDto, Long reviewId, Long commentId, Authentication auth);

    List<CommentDto> getReplies(Long reviewId, Long commentId, Pageable pageable);

    void deleteComment(Long reviewId, Long commentId, UserDetails userDetails);

    PlaceReviewDto updatePlaceReview(PlaceReviewSaveDto placeReviewSaveDto, Long reviewId, Authentication auth);

    void deletePlaceReview(Long reviewId, Authentication auth);
}
