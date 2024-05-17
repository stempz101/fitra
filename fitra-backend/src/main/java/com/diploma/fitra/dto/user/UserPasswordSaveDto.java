package com.diploma.fitra.dto.user;

import com.diploma.fitra.dto.group.OnRecover;
import com.diploma.fitra.dto.group.OnUpdate;
import com.diploma.fitra.dto.validation.Password;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPasswordSaveDto {

    @NotBlank(message = "{validation.not_blank.current_password}", groups = OnUpdate.class)
    private String currentPassword;

    @Password(groups = {OnUpdate.class, OnRecover.class})
    @NotBlank(message = "{validation.not_blank.new_password}", groups = {OnUpdate.class, OnRecover.class})
    private String newPassword;

    @NotBlank(message = "{validation.not_blank.repeat_password}", groups = {OnUpdate.class, OnRecover.class})
    private String repeatPassword;
}
