package com.diploma.fitra.dto.travel;

import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.group.OnUpdate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TravelSaveDto {

    @NotBlank(message = "{validation.not_blank.title}", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @NotNull(message = "{validation.not_null.type_id}", groups = {OnCreate.class, OnUpdate.class})
    private Long typeId;

    @NotBlank(message = "{validation.not_blank.description}", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotNull(message = "{validation.not_null.limit}", groups = {OnCreate.class, OnUpdate.class})
    private Integer limit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "{validation.not_null.start_date}", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate startDate;

    @NotNull(message = "{validation.not_null.creator_id}", groups = OnCreate.class)
    @Null(message = "{validation.null.creator_id}", groups = OnUpdate.class)
    private Long creatorId;

    @Valid
    @NotNull(message = "{validation.not_null.route}", groups = {OnCreate.class, OnUpdate.class})
    private List<RouteDto> route;
}
