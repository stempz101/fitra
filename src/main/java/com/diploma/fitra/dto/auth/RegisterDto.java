package com.diploma.fitra.dto.auth;

import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.validation.Password;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterDto {

    @NotBlank(message = "{validation.not_blank.first_name}", groups = OnCreate.class)
    private String firstName;

    @NotBlank(message = "{validation.not_blank.last_name}", groups = OnCreate.class)
    private String lastName;

    @Email
    @NotBlank(message = "{validation.not_blank.email}", groups = OnCreate.class)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Password
    @NotBlank(message = "{validation.not_blank.password}", groups = OnCreate.class)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "{validation.not_blank.repeat_password}", groups = OnCreate.class)
    private String repeatPassword;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "{validation.not_null.birthday}", groups = OnCreate.class)
    private LocalDate birthday;

    @NotNull(message = "{validation.not_null.country_id}", groups = OnCreate.class)
    private Long countryId;

    private long cityId;

    private String about;
}
