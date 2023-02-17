package com.diploma.fitra.repo;

import com.diploma.fitra.model.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {

    List<Travel> findAllByIsConfirmedIsTrue();
}
