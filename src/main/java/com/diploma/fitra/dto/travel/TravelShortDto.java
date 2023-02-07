package com.diploma.fitra.dto.travel;

import com.diploma.fitra.dto.user.UserShortDto;
import lombok.Data;

@Data
public class TravelShortDto {

    private Long id;
    private String title;
    private UserShortDto creator;
}
