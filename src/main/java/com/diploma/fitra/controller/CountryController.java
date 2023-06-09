package com.diploma.fitra.controller;

import com.diploma.fitra.api.CountryApi;
import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.dto.country.CountryDto;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CountryController implements CountryApi {
    private final CountryService countryService;

    @Override
    public List<CountryDto> getCountries(String search) {
        return countryService.getCountries(search);
    }

    @Override
    public CountryDto getCountry(Long countryId) {
        return countryService.getCountry(countryId);
    }

    @Override
    public List<CityDto> getCities(String search, Pageable pageable) {
        return countryService.getCities(search, pageable);
    }

    @Override
    public List<CityDto> getCitiesByCountry(Long countryId, String search, Pageable pageable) {
        return countryService.getCitiesByCountry(countryId, search, pageable);
    }

    @Override
    public CityDto getCity(Long cityId) {
        return countryService.getCity(cityId);
    }
}
