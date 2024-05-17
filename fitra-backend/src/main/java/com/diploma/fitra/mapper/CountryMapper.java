package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.country.CountryDto;
import com.diploma.fitra.model.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CountryMapper {

    CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);

    @Mapping(source = "titleEn", target = "title")
    CountryDto toCountryDto(Country country);
}
