package com.diploma.fitra.service;

import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.model.Type;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TypeService {

    Type createType(TypeSaveDto typeSaveDto, Authentication auth);

    List<TypeDto> getTypes();

    Type getType(Long typeId);

    Type updateType(Long typeId, TypeSaveDto typeSaveDto, Authentication auth);
}
