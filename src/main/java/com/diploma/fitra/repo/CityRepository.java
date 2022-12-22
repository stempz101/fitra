package com.diploma.fitra.repo;

import com.diploma.fitra.model.City;
import com.diploma.fitra.model.Country;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findAllByCountry(Country country, Sort sort);
}
