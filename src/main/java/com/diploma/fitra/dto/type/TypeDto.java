package com.diploma.fitra.dto.type;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TypeDto {
    private Long id;
    private String name;
    private String alternativeName;
}
