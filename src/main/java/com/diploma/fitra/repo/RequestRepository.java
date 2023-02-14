package com.diploma.fitra.repo;

import com.diploma.fitra.model.Request;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByUser(User user);

    List<Request> findAllByTravelAndUser(Travel travel, User user, Sort sort);

    List<Request> findAllByTravel_Creator(User creator);
}
