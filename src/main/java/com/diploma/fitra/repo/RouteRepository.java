package com.diploma.fitra.repo;

import com.diploma.fitra.model.Route;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.key.RouteKey;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, RouteKey> {

    List<Route> findAllByTravel(Travel travel, Sort sort);
}
