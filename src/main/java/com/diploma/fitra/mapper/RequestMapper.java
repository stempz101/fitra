package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.model.JoinRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(source = "user", target = "sender")
    @Mapping(source = "user.fullName", target = "sender.name")
    @Mapping(source = "user.firstName", target = "sender.firstName")
    @Mapping(source = "travel.creator.fullName", target = "travel.creator.name")
    @Mapping(source = "travel.creator.firstName", target = "travel.creator.firstName")
    RequestDto toRequestDto(JoinRequest request);
}
