package com.diploma.fitra.repo;

import com.diploma.fitra.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
