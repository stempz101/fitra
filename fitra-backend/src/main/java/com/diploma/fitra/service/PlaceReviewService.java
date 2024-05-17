package com.diploma.fitra.service;

import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.dto.comment.CommentSaveDto;
import com.diploma.fitra.dto.placereview.PlaceReviewDto;
import com.diploma.fitra.dto.placereview.PlaceReviewSaveDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface PlaceReviewService {

    PlaceReviewDto createPlaceReview(PlaceReviewSaveDto placeReviewSaveDto, UserDetails userDetails);

    List<PlaceReviewDto> getPlaceReviews(Pageable pageable, UserDetails userDetails);

    PlaceReviewDto getPlaceReview(Long reviewId, UserDetails userDetails);

    void setLike(Long reviewId, UserDetails userDetails);

    CommentDto createComment(CommentSaveDto commentSaveDto, Long reviewId, UserDetails userDetails);

    List<CommentDto> getComments(Long reviewId, Pageable pageable);

    CommentDto createReply(CommentSaveDto commentSaveDto, Long reviewId, Long commentId, UserDetails userDetails);

    List<CommentDto> getReplies(Long reviewId, Long commentId, Pageable pageable);

    void deleteComment(Long reviewId, Long commentId, UserDetails userDetails);

    PlaceReviewDto updatePlaceReview(PlaceReviewSaveDto placeReviewSaveDto, Long reviewId, UserDetails userDetails);

    void deletePlaceReview(Long reviewId, UserDetails userDetails);
}
