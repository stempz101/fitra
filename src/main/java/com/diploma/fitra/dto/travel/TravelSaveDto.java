package com.diploma.fitra.dto.travel;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TravelSaveDto {

    @NotBlank(message = "{validation.not_blank.title}")
    private String title;

    @NotNull(message = "{validation.not_null.type_id}")
    private Long typeId;

    @NotBlank(message = "{validation.not_blank.description}")
    private String description;

    @NotNull(message = "{validation.not_null.limit}")
    private Integer limit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "{validation.not_null.start_date}")
    private LocalDate startDate;

    @NotNull(message = "{validation.not_null.creator_id}")
    private Long creatorId;

    @NotNull(message = "{validation.not_null.route}")
    private List<RouteDto> route;
}
