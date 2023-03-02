package com.diploma.fitra.service;

import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.model.Type;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TypeService {

    Type createType(TypeSaveDto typeSaveDto, UserDetails userDetails);

    List<TypeDto> getTypes();

    Type getType(Long typeId);

    Type updateType(Long typeId, TypeSaveDto typeSaveDto, UserDetails userDetails);
}
