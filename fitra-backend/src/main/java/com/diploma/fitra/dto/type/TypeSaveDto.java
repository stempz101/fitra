package com.diploma.fitra.dto.type;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TypeSaveDto {

    @NotBlank(message = "{validation.not_blank.name_en}")
    private String nameEn;

    @NotBlank(message = "{validation.not_blank.name_ua}")
    private String nameUa;
}
