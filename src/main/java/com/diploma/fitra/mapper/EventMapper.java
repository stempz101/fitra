package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.travel.EventDto;
import com.diploma.fitra.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    Event fromEventDto(EventDto eventDto);

    EventDto toEventDto(Event event);
}
