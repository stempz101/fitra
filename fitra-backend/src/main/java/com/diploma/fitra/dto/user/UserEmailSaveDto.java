package com.diploma.fitra.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserEmailSaveDto {

    @Email
    @NotBlank(message = "{validation.not_blank.email}")
    private String email;
}
