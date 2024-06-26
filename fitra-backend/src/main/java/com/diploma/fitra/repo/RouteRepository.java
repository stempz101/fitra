package com.diploma.fitra.repo;

import com.diploma.fitra.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findAllByTravelIdOrderByPositionAsc(Long travelId);

    void deleteAllByTravelId(Long travelId);
}
