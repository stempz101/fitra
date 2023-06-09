package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.model.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParticipantMapper {

    ParticipantMapper INSTANCE = Mappers.getMapper(ParticipantMapper.class);

    @Mapping(source = "user.fullName", target = "user.name")
    @Mapping(source = "user.firstName", target = "user.firstName")
    ParticipantDto toParticipantDto(Participant participant);
}
