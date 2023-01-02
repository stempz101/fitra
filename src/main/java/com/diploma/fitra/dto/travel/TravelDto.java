package com.diploma.fitra.dto.travel;

import com.diploma.fitra.dto.type.TypeDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TravelDto {
    private Long id;
    private String title;
    private TypeDto type;
    private String description;
    private Integer limit;
    private LocalDate startDate;
    private List<RouteDto> route;
}
