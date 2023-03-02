package com.diploma.fitra.repo;

import com.diploma.fitra.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findAllByUserIdOrderByCreateTimeDesc(Long userId);

    List<Invitation> findAllByTravel_CreatorIdOrderByCreateTimeDesc(Long creatorId);

    List<Invitation> findAllByTravelIdAndUserIdOrderByCreateTimeDesc(Long travelId, Long userId);
}
