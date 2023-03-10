package com.diploma.fitra.dto.travel;

import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.group.OnUpdate;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventSaveDto {

    @NotBlank(message = "{validation.not_blank.name}", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(message = "{validation.not_null.start_time}", groups = {OnCreate.class, OnUpdate.class})
    @FutureOrPresent(message = "{validation.future_or_present.start_time}", groups = OnCreate.class)
    private LocalDateTime startTime;

    @NotNull(message = "{validation.not_null.end_time}", groups = {OnCreate.class, OnUpdate.class})
    @Future(message = "{validation.future.end_time}", groups = OnCreate.class)
    private LocalDateTime endTime;
}
