package com.diploma.fitra.dto.user;

import com.diploma.fitra.dto.validation.Password;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPasswordSaveDto {

    @NotBlank(message = "{validation.not_blank.current_password}")
    private String currentPassword;

    @Password
    @NotBlank(message = "{validation.not_blank.new_password}")
    private String newPassword;

    @NotBlank(message = "{validation.not_blank.repeat_password}")
    private String repeatPassword;
}
