package com.diploma.fitra.dto.travel;

import com.diploma.fitra.dto.user.UserShortDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TravelShortDto {
    private Long id;
    private String name;
    private UserShortDto creator;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<RouteDto> route;
}
