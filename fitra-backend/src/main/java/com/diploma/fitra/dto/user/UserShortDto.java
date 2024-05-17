package com.diploma.fitra.dto.user;

import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.dto.country.CountryDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserShortDto {

    private Long id;
    private String firstName;
    private String name;
    private LocalDate birthday;
    private CountryDto country;
    private CityDto cityDto;
    private String about;
    private Boolean isAdmin;
    private Boolean isEnabled;
    private Boolean isBlocked;
}
