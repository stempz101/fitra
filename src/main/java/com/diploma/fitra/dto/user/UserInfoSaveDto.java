package com.diploma.fitra.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInfoSaveDto {

    @NotBlank(message = "{validation.not_blank.first_name}")
    private String firstName;

    @NotBlank(message = "{validation.not_blank.last_name}")
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "{validation.not_null.birthday}")
    private LocalDate birthday;

    @NotNull(message = "{validation.not_null.country_id}")
    private Long countryId;

    private Long cityId;
}
