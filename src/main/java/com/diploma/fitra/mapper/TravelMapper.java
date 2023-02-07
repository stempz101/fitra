package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import com.diploma.fitra.model.Travel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TravelMapper {

    TravelMapper INSTANCE = Mappers.getMapper(TravelMapper.class);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "peopleLimit", target = "limit")
    @Mapping(source = "startDate", target = "startDate")
    TravelDto toTravelDto(Travel travel);

    @Mapping(source = "limit", target = "peopleLimit")
    Travel fromTravelSaveDto(TravelSaveDto travelSaveDto);
}
