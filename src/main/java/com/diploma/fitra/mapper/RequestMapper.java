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
    RequestDto toRequestDto(JoinRequest request);
}
