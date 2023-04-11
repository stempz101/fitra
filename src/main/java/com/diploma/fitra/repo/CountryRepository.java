package com.diploma.fitra.repo;

import com.diploma.fitra.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    List<Country> findAllByTitleEnContainingIgnoreCaseOrderById(String title);

    List<Country> findAllByTitleUaContainingIgnoreCaseOrderById(String title);
}
