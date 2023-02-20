package com.diploma.fitra.repo;

import com.diploma.fitra.model.JoinRequest;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {

    List<JoinRequest> findAllByUser(User user);

    List<JoinRequest> findAllByTravelAndUser(Travel travel, User user, Sort sort);

    List<JoinRequest> findAllByTravel_Creator(User creator);
}
