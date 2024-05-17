package com.diploma.fitra.dto.user;

import com.diploma.fitra.dto.validation.Password;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class UserSaveDto {

    @NotBlank(message = "{validation.not_blank.first_name}")
    private String firstName;

    @NotBlank(message = "{validation.not_blank.last_name}")
    private String lastName;

    @Email
    @NotBlank(message = "{validation.not_blank.email}")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Password
    @NotBlank(message = "{validation.not_blank.password}")
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "{validation.not_blank.repeat_password}")
    private String repeatPassword;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "{validation.not_null.birthday}")
    private LocalDate birthday;

    @NotNull(message = "{validation.not_null.country_id}")
    private Long countryId;

    private Long cityId;

    private String about;

    private MultipartFile avatar;
}
