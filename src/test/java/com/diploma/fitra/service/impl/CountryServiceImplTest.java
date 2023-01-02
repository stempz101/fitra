package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.dto.country.CountryDto;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.model.City;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.repo.CityRepository;
import com.diploma.fitra.repo.CountryRepository;
import com.diploma.fitra.test.util.CityDataTest;
import com.diploma.fitra.test.util.CountryDataTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryServiceImplTest {

    @InjectMocks
    private CountryServiceImpl countryService;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CityRepository cityRepository;

    @Test
    void getCountriesTest() {
        List<Country> countries = new ArrayList<>();
        countries.add(CountryDataTest.getCountry1());
        countries.add(CountryDataTest.getCountry2());
        countries.add(CountryDataTest.getCountry3());
        countries.add(CountryDataTest.getCountry4());
        String search = "";

        when(countryRepository.findAllByTitleEnContainingIgnoreCase(any(), any())).thenReturn(countries);
        List<CountryDto> result = countryService.getCountries(search);

        assertThat(result, hasSize(countries.size()));
        assertThat(result, hasItems(
                CountryDataTest.getCountryDto1(),
                CountryDataTest.getCountryDto2(),
                CountryDataTest.getCountryDto3(),
                CountryDataTest.getCountryDto4()
        ));
    }

    @Test
    void getCountryTest() {
        Country country = CountryDataTest.getCountry1();

        when(countryRepository.findById(any())).thenReturn(Optional.of(country));
        Country result = countryService.getCountry(country.getId());

        assertThat(result, allOf(
                hasProperty("id", equalTo(country.getId())),
                hasProperty("titleEn", equalTo(country.getTitleEn())),
                hasProperty("titleUa", equalTo(country.getTitleUa()))
        ));
    }

    @Test
    void getCountryWithNotFoundException() {
        Country country = CountryDataTest.getCountry1();

        when(countryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> countryService.getCountry(country.getId()));
    }

    @Test
    void getCitiesTest() {
        Country country = CountryDataTest.getCountry1();
        List<City> cities = new ArrayList<>();
        cities.add(CityDataTest.getCity1());
        cities.add(CityDataTest.getCity2());
        cities.add(CityDataTest.getCity3());
        cities.add(CityDataTest.getCity4());
        String search = "";

        when(countryRepository.findById(any())).thenReturn(Optional.of(country));
        when(cityRepository.findAllByCountryAndTitleEnContainingIgnoreCase(any(), any(), any())).thenReturn(cities);
        List<CityDto> result = countryService.getCities(country.getId(), search);

        assertThat(result, hasSize(cities.size()));
        assertThat(result, hasItems(
                CityDataTest.getCityDto1(),
                CityDataTest.getCityDto2(),
                CityDataTest.getCityDto3(),
                CityDataTest.getCityDto4()
        ));
    }

    @Test
    void getCitiesWithCountryNotFoundException() {
        Country country = CountryDataTest.getCountry1();
        String search = "";

        when(countryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> countryService.getCities(country.getId(), search));
    }
}
