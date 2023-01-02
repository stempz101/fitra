package com.diploma.fitra.service;

import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.model.Type;

import java.util.List;

public interface TypeService {

    Type createType(TypeSaveDto typeSaveDto);

    List<TypeDto> getTypes();

    Type getType(Long typeId);

    Type updateType(Long typeId, TypeSaveDto typeSaveDto);
}
