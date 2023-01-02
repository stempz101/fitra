package com.diploma.fitra.test.util;

import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.model.Type;

public class TypeDataTest {

    private static final Long TYPE_1_ID = 1L;
    private static final String TYPE_1_NAME_EN = "Beach";
    private static final String TYPE_1_NAME_UA = "Пляж";

    private static final Long TYPE_2_ID = 2L;
    private static final String TYPE_2_NAME_EN = "Tours";
    private static final String TYPE_2_NAME_UA = "Екскурсії";

    private static final Long TYPE_3_ID = 3L;
    private static final String TYPE_3_NAME_EN = "Cruises";
    private static final String TYPE_3_NAME_UA = "Круїзи";

    private static final Long TYPE_4_ID = 4L;
    private static final String TYPE_4_NAME_EN = "Hiking";
    private static final String TYPE_4_NAME_UA = "Походи";

    public static Type getType1() {
        Type type = new Type();
        type.setId(TYPE_1_ID);
        type.setNameEn(TYPE_1_NAME_EN);
        type.setNameUa(TYPE_1_NAME_UA);
        return type;
    }

    public static Type getType2() {
        Type type = new Type();
        type.setId(TYPE_2_ID);
        type.setNameEn(TYPE_2_NAME_EN);
        type.setNameUa(TYPE_2_NAME_UA);
        return type;
    }

    public static Type getType3() {
        Type type = new Type();
        type.setId(TYPE_3_ID);
        type.setNameEn(TYPE_3_NAME_EN);
        type.setNameUa(TYPE_3_NAME_UA);
        return type;
    }

    public static Type getType4() {
        Type type = new Type();
        type.setId(TYPE_4_ID);
        type.setNameEn(TYPE_4_NAME_EN);
        type.setNameUa(TYPE_4_NAME_UA);
        return type;
    }

    public static TypeDto getTypeDto1() {
        TypeDto typeDto = new TypeDto();
        typeDto.setId(TYPE_1_ID);
        typeDto.setName(TYPE_1_NAME_EN);
        return typeDto;
    }

    public static TypeDto getTypeDto2() {
        TypeDto typeDto = new TypeDto();
        typeDto.setId(TYPE_2_ID);
        typeDto.setName(TYPE_2_NAME_EN);
        return typeDto;
    }

    public static TypeDto getTypeDto3() {
        TypeDto typeDto = new TypeDto();
        typeDto.setId(TYPE_3_ID);
        typeDto.setName(TYPE_3_NAME_EN);
        return typeDto;
    }

    public static TypeDto getTypeDto4() {
        TypeDto typeDto = new TypeDto();
        typeDto.setId(TYPE_4_ID);
        typeDto.setName(TYPE_4_NAME_EN);
        return typeDto;
    }

    public static TypeSaveDto getTypeSaveDto1() {
        TypeSaveDto typeSaveDto = new TypeSaveDto();
        typeSaveDto.setNameEn(TYPE_1_NAME_EN);
        typeSaveDto.setNameUa(TYPE_1_NAME_UA);
        return typeSaveDto;
    }
}
