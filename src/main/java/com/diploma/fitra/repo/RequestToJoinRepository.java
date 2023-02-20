package com.diploma.fitra.repo;

import com.diploma.fitra.model.RequestToJoin;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestToJoinRepository extends JpaRepository<RequestToJoin, Long> {

    List<RequestToJoin> findAllByUser(User user);

    List<RequestToJoin> findAllByTravelAndUser(Travel travel, User user, Sort sort);

    List<RequestToJoin> findAllByTravel_Creator(User creator);
}
