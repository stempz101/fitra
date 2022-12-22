package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.dto.country.CountryDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.CityMapper;
import com.diploma.fitra.mapper.CountryMapper;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.CityRepository;
import com.diploma.fitra.repo.CountryRepository;
import com.diploma.fitra.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    @Override
    public List<CountryDto> getCountries(String lang, String search) {
        if (lang == null || lang.isEmpty() || lang.equals("en")) {
            return countryRepository
                    .findAllByTitleEnContainingIgnoreCase(search, Sort.by("titleEn"))
                    .stream()
                    .map(country -> {
                        CountryDto countryDto = CountryMapper.INSTANCE.toCountryDto(country);
                        countryDto.setTitle(country.getTitleEn());
                        return countryDto;
                    })
                    .collect(Collectors.toList());
        } else if (lang.equals("ua")) {
            return countryRepository
                    .findAllByTitleUaContainingIgnoreCase(search, Sort.by("titleUa"))
                    .stream()
                    .map(country -> {
                        CountryDto countryDto = CountryMapper.INSTANCE.toCountryDto(country);
                        countryDto.setTitle(country.getTitleUa());
                        return countryDto;
                    })
                    .collect(Collectors.toList());
        } else {
            throw new BadRequestException(Error.NOT_SUPPORTED_LANGUAGE.getMessage());
        }
    }

    @Override
    public Country getCountry(Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Error.COUNTRY_NOT_FOUND.getMessage()));
    }

    @Override
    public List<CityDto> getCities(Long countryId, String search) {
        Country country = getCountry(countryId);
        return cityRepository
                .findAllByCountryAndTitleEnContainingIgnoreCase(country, search, Sort.by("titleEn"))
                .stream()
                .map(CityMapper.INSTANCE::toCityDto)
                .collect(Collectors.toList());
    }
}
