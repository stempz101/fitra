package com.diploma.fitra.dto.invitation;

import com.diploma.fitra.dto.travel.TravelShortDto;
import com.diploma.fitra.dto.user.UserShortDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvitationDto {

    private Long id;
    private TravelShortDto travel;
    private UserShortDto receiver;
    private LocalDateTime createTime;
    private String status;
}
