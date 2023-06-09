package com.diploma.fitra.dto.user;

import com.diploma.fitra.dto.country.CityDto;
import com.diploma.fitra.dto.country.CountryDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String name;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String email;
    private String about;
    private double rating;
    private long commentCount;
    private CountryDto country;
    private CityDto city;
    private Boolean isAdmin;
}
