package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.model.Type;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TypeMapper {

    TypeMapper INSTANCE = Mappers.getMapper(TypeMapper.class);

    @Mapping(source = "nameEn", target = "name")
    TypeDto toTypeDto(Type type);

    Type fromTypeSaveDto(TypeSaveDto typeSaveDto);
}
