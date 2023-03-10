package com.diploma.fitra.dto.travel;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDto {

    private Long id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
