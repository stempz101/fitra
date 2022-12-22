package com.diploma.fitra.service;

import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.dto.country.CountryDto;
import com.diploma.fitra.model.Country;

import java.util.List;

public interface CountryService {

    List<CountryDto> getCountries(String lang, String search);

    Country getCountry(Long id);

    List<CityDto> getCities(Long countryId);
}
