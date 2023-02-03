package com.diploma.fitra.dto.success;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessDto {

    private int statusCode;
    private Object result;
}
