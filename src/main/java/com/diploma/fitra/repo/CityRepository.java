package com.diploma.fitra.repo;

import com.diploma.fitra.model.City;
import com.diploma.fitra.model.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("select c from _cities c where upper(c.titleEn) like upper(concat(?1, '%'))")
    List<City> findAllByTitleEnContainingIgnoreCase(String search, Pageable pageable);

    @Query("select c from _cities c where c.country = ?1 and upper(c.titleEn) like upper(concat(?2, '%'))")
    List<City> findAllByCountryAndTitleEnContainingIgnoreCase(Country country, String search, Pageable pageable);
}
