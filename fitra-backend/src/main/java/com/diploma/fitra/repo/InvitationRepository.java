package com.diploma.fitra.repo;

import com.diploma.fitra.model.Invitation;
import com.diploma.fitra.model.enums.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findAllByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);

    List<Invitation> findAllByTravelIdOrderByCreateTimeDesc(Long travelId);

    boolean existsByUserIdAndTravelIdAndStatus(Long userId, Long travelId, Status status);

    long countByUserIdAndViewedIsFalse(Long userId);
}
