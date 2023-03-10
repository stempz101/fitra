package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.travel.EventDto;
import com.diploma.fitra.dto.travel.EventSaveDto;
import com.diploma.fitra.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {

    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    Event fromEventSaveDto(EventSaveDto eventSaveDto);

    EventDto toEventDto(Event event);
}
