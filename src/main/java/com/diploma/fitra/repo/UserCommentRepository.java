package com.diploma.fitra.repo;

import com.diploma.fitra.model.UserComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCommentRepository extends JpaRepository<UserComment, Long> {

    List<UserComment> findAllByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);

    boolean existsByUserIdAndAuthorId(Long userId, Long authorId);

    long countByUserId(Long userId);

    @Query("select coalesce(avg(uc.rating), 0) from _user_comments uc where uc.user.id = ?1")
    double avgRatingByUserId(Long userId);
}
