package com.diploma.fitra.dto.request;

import com.diploma.fitra.dto.travel.TravelShortDto;
import com.diploma.fitra.dto.user.UserShortDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {

    private Long id;
    private TravelShortDto travel;
    private UserShortDto sender;
    private LocalDateTime createTime;
    private String status;
}
