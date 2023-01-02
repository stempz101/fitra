package com.diploma.fitra.controller;

import com.diploma.fitra.api.CountryApi;
import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.dto.country.CountryDto;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CountryController implements CountryApi {
    private final CountryService countryService;

    public List<CountryDto> getCountries(String search) {
        return countryService.getCountries(search);
    }

    public Country getCountry(Long countryId) {
        return countryService.getCountry(countryId);
    }

    public List<CityDto> getCities(Long countryId, String search) {
        return countryService.getCities(countryId, search);
    }
}
