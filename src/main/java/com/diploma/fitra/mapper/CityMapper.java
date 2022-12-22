package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CityMapper {

    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    @Mapping(source = "titleEn", target = "title")
    CityDto toCityDto(City city);
}
