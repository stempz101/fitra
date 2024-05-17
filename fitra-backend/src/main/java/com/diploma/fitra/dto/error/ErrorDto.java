package com.diploma.fitra.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorDto {

    private String message;
    private LocalDateTime timestamp;
}
