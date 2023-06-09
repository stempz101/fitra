package com.diploma.fitra.service;

import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.dto.country.CountryDto;
import com.diploma.fitra.model.Country;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CountryService {

    List<CountryDto> getCountries(String search);

    CountryDto getCountry(Long id);

    List<CityDto> getCities(String search, Pageable pageable);

    List<CityDto> getCitiesByCountry(Long countryId, String search, Pageable pageable);

    CityDto getCity(Long cityId);
}
