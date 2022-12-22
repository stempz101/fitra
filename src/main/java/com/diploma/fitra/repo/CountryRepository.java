package com.diploma.fitra.repo;

import com.diploma.fitra.model.Country;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    List<Country> findAllByTitleEnContainingIgnoreCase(String title, Sort sort);

    List<Country> findAllByTitleUaContainingIgnoreCase(String title, Sort sort);
}
