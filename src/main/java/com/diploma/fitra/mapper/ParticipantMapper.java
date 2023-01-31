package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.model.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParticipantMapper {

    ParticipantMapper INSTANCE = Mappers.getMapper(ParticipantMapper.class);

    @Mapping(source = "travel.id", target = "travelId")
    @Mapping(source = "user.country.titleEn", target = "user.country.title")
    @Mapping(source = "user.city.titleEn", target = "user.city.title")
//    @Mapping(source = "user.admin", target = "user.isAdmin")
    @Mapping(source = "creator", target = "isCreator")
    ParticipantDto toParticipantDto(Participant participant);
}
