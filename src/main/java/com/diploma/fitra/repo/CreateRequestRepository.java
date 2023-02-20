package com.diploma.fitra.repo;

import com.diploma.fitra.model.CreateRequest;
import com.diploma.fitra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreateRequestRepository extends JpaRepository<CreateRequest, Long> {

    List<CreateRequest> findAllByTravel_Creator(User creator);
}
