package com.diploma.fitra.repo;

import com.diploma.fitra.model.JoinRequest;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.enums.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    List<JoinRequest> findAllByUserIdOrderByCreateTime(Long userId, Pageable pageable);

    boolean existsByUserIdAndTravelIdAndStatus(Long userId, Long travelId, Status status);

    long countByTravel_CreatorIdAndViewedIsFalse(Long creatorId);

    long countByTravelIdAndViewedIsFalse(Long travelId);

    List<JoinRequest> findAllByTravelAndUser(Travel travel, User user, Sort sort);

    List<JoinRequest> findAllByTravelIdOrderByCreateTime(Long travelId);
}
