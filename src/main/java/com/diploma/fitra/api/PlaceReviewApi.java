package com.diploma.fitra.api;

import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.dto.comment.CommentSaveDto;
import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.group.OnUpdate;
import com.diploma.fitra.dto.placereview.PlaceReviewDto;
import com.diploma.fitra.dto.placereview.PlaceReviewSaveDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/place-reviews")
public interface PlaceReviewApi {

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    PlaceReviewDto createPlaceReview(@RequestBody @Validated(OnCreate.class) PlaceReviewSaveDto placeReviewSaveDto,
                                     @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping
    @SecurityRequirement(name = "Bearer Authentication")
    List<PlaceReviewDto> getPlaceReviews(@PageableDefault(sort = "createDate", size = 6, direction = Sort.Direction.DESC)
                                         Pageable pageable,
                                         @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/{reviewId}")
    @SecurityRequirement(name = "Bearer Authentication")
    PlaceReviewDto getPlaceReview(@PathVariable Long reviewId,
                                  @AuthenticationPrincipal UserDetails userDetails);

    @PostMapping("/{reviewId}/like")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> setLike(@PathVariable Long reviewId,
                                 @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/{reviewId}/comments")
    List<CommentDto> getComments(@PathVariable Long reviewId,
                                 @PageableDefault Pageable pageable);

    @PostMapping("/{reviewId}/comments")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    CommentDto createComment(@RequestBody @Valid CommentSaveDto commentSaveDto,
                             @PathVariable Long reviewId,
                             @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/{reviewId}/comments/{commentId}/replies")
    List<CommentDto> getReplies(@PathVariable Long reviewId,
                                @PathVariable Long commentId,
                                @PageableDefault Pageable pageable);

    @PostMapping("/{reviewId}/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    CommentDto createReply(@RequestBody @Valid CommentSaveDto commentSaveDto,
                           @PathVariable Long reviewId,
                           @PathVariable Long commentId,
                           @AuthenticationPrincipal UserDetails userDetails);

    @DeleteMapping("/{reviewId}/comments/{commentId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> deleteComment(@PathVariable Long reviewId,
                                       @PathVariable Long commentId,
                                       @AuthenticationPrincipal UserDetails userDetails);

    @PutMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    PlaceReviewDto updatePlaceReview(@RequestBody @Validated(OnUpdate.class) PlaceReviewSaveDto placeReviewSaveDto,
                                     @PathVariable Long reviewId,
                                     @AuthenticationPrincipal UserDetails userDetails);

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<Void> deletePlaceReview(@PathVariable Long reviewId,
                                           @AuthenticationPrincipal UserDetails userDetails);
}
