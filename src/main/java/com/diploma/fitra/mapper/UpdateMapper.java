package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.model.Type;

public interface UpdateMapper {

    static Type updateTypeWithPresentTypeSaveDtoFields(Type type, TypeSaveDto typeSaveDto) {
        if (type == null)
            return null;

        type.setNameEn(typeSaveDto.getNameEn());
        type.setNameUa(typeSaveDto.getNameUa());

        return type;
    }
}
