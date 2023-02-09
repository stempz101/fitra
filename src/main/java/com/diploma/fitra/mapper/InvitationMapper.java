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
    InvitationDto toInvitationDto(Invitation invitation);
}
