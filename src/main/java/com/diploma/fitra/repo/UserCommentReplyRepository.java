package com.diploma.fitra.repo;

import com.diploma.fitra.model.UserCommentReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCommentReplyRepository extends JpaRepository<UserCommentReply, Long> {

    List<UserCommentReply> findAllByCommentIdOrderByCreateTimeAsc(Long commentId);

    long countByCommentId(Long commentId);
}
