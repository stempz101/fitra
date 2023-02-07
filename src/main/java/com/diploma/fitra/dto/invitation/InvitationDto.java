package com.diploma.fitra.dto.invitation;

import com.diploma.fitra.dto.travel.TravelShortDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvitationDto {

    private TravelShortDto travel;
    private LocalDateTime createTime;
    private String status;
}
