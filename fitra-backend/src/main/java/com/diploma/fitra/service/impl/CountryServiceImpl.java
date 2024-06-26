package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.dto.country.CountryDto;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.CityMapper;
import com.diploma.fitra.mapper.CountryMapper;
import com.diploma.fitra.model.City;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.CityRepository;
import com.diploma.fitra.repo.CountryRepository;
import com.diploma.fitra.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    @Override
    public List<CountryDto> getCountries(String search) {
        return countryRepository
                .findAllByTitleEnContainingIgnoreCaseOrderById(search)
                .stream()
                .map(country -> {
                    CountryDto countryDto = CountryMapper.INSTANCE.toCountryDto(country);
                    countryDto.setTitle(country.getTitleEn());
                    return countryDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CountryDto getCountry(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.COUNTRY_NOT_FOUND.getMessage()));

        CountryDto countryDto = CountryMapper.INSTANCE.toCountryDto(country);
        countryDto.setTitle(country.getTitleEn());
        return countryDto;
    }

    @Override
    public List<CityDto> getCities(String search, Pageable pageable) {
        return cityRepository.findAllByTitleEnContainingIgnoreCase(search, pageable).stream()
                .map(CityMapper.INSTANCE::toCityDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CityDto> getCitiesByCountry(Long countryId, String search, Pageable pageable) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new NotFoundException(Error.COUNTRY_NOT_FOUND.getMessage()));

        return cityRepository
                .findAllByCountryAndTitleEnContainingIgnoreCase(country, search, pageable)
                .stream()
                .map(CityMapper.INSTANCE::toCityDto)
                .collect(Collectors.toList());
    }

    @Override
    public CityDto getCity(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new NotFoundException(Error.CITY_NOT_FOUND.getMessage()));
        return CityMapper.INSTANCE.toCityDto(city);
    }
}
