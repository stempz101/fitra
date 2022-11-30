package com.diploma.fitra.repo;

import com.diploma.fitra.model.Route;
import com.diploma.fitra.model.key.RouteKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, RouteKey> {
}
