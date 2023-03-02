package com.diploma.fitra.dto.travel;

import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.user.UserShortDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TravelDto {

    private Long id;
    private String name;
    private String description;
    private TypeDto type;
    private UserShortDto creator;
    private LocalDate startDate;
    private LocalDate endDate;
    private double budget;
    private int limit;
    private int ageFrom;
    private int ageTo;
    private boolean withChildren;
    private LocalDateTime createdTime;
    private List<RouteDto> route;
}
