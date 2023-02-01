package com.diploma.fitra.controller;

import com.diploma.fitra.api.TypeApi;
import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.model.Type;
import com.diploma.fitra.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TypeController implements TypeApi {
    private final TypeService typeService;

    @Override
    public Type createType(TypeSaveDto typeSaveDto) {
        return typeService.createType(typeSaveDto);
    }

    @Override
    public List<TypeDto> getTypes() {
        return typeService.getTypes();
    }

    @Override
    public Type getType(Long typeId) {
        return typeService.getType(typeId);
    }

    @Override
    public Type updateType(Long typeId, TypeSaveDto typeSaveDto) {
        return typeService.updateType(typeId, typeSaveDto);
    }
}
