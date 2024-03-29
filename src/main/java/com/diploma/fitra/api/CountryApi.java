package com.diploma.fitra.api;

import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.dto.country.CountryDto;
import com.diploma.fitra.model.Country;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/v1/countries")
public interface CountryApi {

    @GetMapping
    List<CountryDto> getCountries(@RequestParam(defaultValue = "") String search);

    @GetMapping("/{countryId}")
    CountryDto getCountry(@PathVariable Long countryId);

    @GetMapping("/cities")
    List<CityDto> getCities(@RequestParam(defaultValue = "") String search,
                            @PageableDefault(size = 50, sort = {"id"}) Pageable pageable);

    @GetMapping("/{countryId}/cities")
    List<CityDto> getCitiesByCountry(@PathVariable Long countryId,
                                     @RequestParam(defaultValue = "") String search,
                                     @PageableDefault(size = 50, sort = {"id"}) Pageable pageable);

    @GetMapping("/cities/{cityId}")
    CityDto getCity(@PathVariable Long cityId);
}
