package com.diploma.fitra.repo;

import com.diploma.fitra.model.Route;
import com.diploma.fitra.model.Travel;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findAllByTravel(Travel travel, Sort sort);

    void deleteAllByTravel(Travel travel);
}
