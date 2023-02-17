package com.diploma.fitra.repo;

import com.diploma.fitra.model.RequestToCreate;
import com.diploma.fitra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestToCreateRepository extends JpaRepository<RequestToCreate, Long> {

    List<RequestToCreate> findAllByTravel_Creator(User creator);
}
