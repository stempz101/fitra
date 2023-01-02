package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.travel.RouteDto;
import com.diploma.fitra.model.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RouteMapper {

    RouteMapper INSTANCE = Mappers.getMapper(RouteMapper.class);

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.titleEn", target = "country")
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.titleEn", target = "city")
    RouteDto toRouteDto(Route route);
}
