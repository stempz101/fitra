package com.diploma.fitra.controller;

import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.dto.country.CountryDto;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/country")
@RequiredArgsConstructor
public class CountryController {
    private final CountryService countryService;

    @GetMapping
    public List<CountryDto> getCountries(@RequestParam(defaultValue = "") String search) {
        return countryService.getCountries(null, search);
    }

    @GetMapping("/{lang}")
    public List<CountryDto> getCountries(@PathVariable String lang, @RequestParam(defaultValue = "") String search) {
        return countryService.getCountries(lang, search);
    }

    @GetMapping("/id/{countryId}")
    public Country getCountry(@PathVariable Long countryId) {
        return countryService.getCountry(countryId);
    }

    @GetMapping("/{countryId}/city")
    public List<CityDto> getCities(@PathVariable Long countryId, @RequestParam(defaultValue = "") String search) {
        return countryService.getCities(countryId, search);
    }
}
