package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.invitation.InvitationDto;
import com.diploma.fitra.model.Invitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvitationMapper {

    InvitationMapper INSTANCE = Mappers.getMapper(InvitationMapper.class);

    @Mapping(source = "user", target = "receiver")
    @Mapping(source = "user.fullName", target = "receiver.name")
    @Mapping(source = "user.firstName", target = "receiver.firstName")
    @Mapping(source = "travel.creator.fullName", target = "travel.creator.name")
    @Mapping(source = "travel.creator.firstName", target = "travel.creator.firstName")
    InvitationDto toInvitationDto(Invitation invitation);
}
