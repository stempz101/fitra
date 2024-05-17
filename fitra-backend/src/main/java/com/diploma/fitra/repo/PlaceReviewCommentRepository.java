package com.diploma.fitra.repo;

import com.diploma.fitra.model.PlaceReviewComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceReviewCommentRepository extends JpaRepository<PlaceReviewComment, Long> {

    List<PlaceReviewComment> findAllByPlaceReviewIdAndParentCommentIsNullOrderByCreateDateAsc(Long reviewId, Pageable pageable);

    List<PlaceReviewComment> findAllByPlaceReviewIdAndParentCommentIdOrderByCreateDateAsc(Long reviewId, Long commentId, Pageable pageable);

    long countByParentCommentId(Long commentId);
}
