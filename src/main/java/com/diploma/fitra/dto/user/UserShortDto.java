package com.diploma.fitra.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserShortDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Boolean isAdmin;
}
