package com.diploma.fitra.dto.travel;

import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.group.OnUpdate;
import com.diploma.fitra.dto.validation.NoLink;
import com.diploma.fitra.dto.validation.NoPhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class TravelSaveDto {

    @NotBlank(message = "{validation.not_blank.title}", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(message = "{validation.not_blank.description}", groups = {OnCreate.class, OnUpdate.class})
    @NoLink(message = "{validation.no_link.description}", groups = {OnCreate.class, OnUpdate.class})
    @NoPhoneNumber(message = "{validation.no_phone_number.description}", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotNull(message = "{validation.not_null.type_id}", groups = {OnCreate.class, OnUpdate.class})
    private Long typeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "{validation.not_null.start_date}", groups = {OnCreate.class, OnUpdate.class})
    @FutureOrPresent(message = "{validation.future_or_present.start_date}", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "{validation.not_null.end_date}", groups = {OnCreate.class, OnUpdate.class})
    @Future(message = "{validation.future.end_date}", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate endDate;

    @NotNull(message = "{validation.not_null.budget}", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 0, message = "{validation.min.budget}", groups = {OnCreate.class, OnUpdate.class})
    private Double budget;

    @NotNull(message = "{validation.not_null.limit}", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 2, message = "{validation.min.limit}", groups = {OnCreate.class, OnUpdate.class})
    private Integer limit;

    @NotNull(message = "{validation.not_null.age_from}", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 0, message = "{validation.min.age_from}", groups = {OnCreate.class, OnUpdate.class})
    private Integer ageFrom;

    @NotNull(message = "{validation.not_null.age_to}", groups = {OnCreate.class, OnUpdate.class})
    @Min(value = 0, message = "{validation.min.age_to}", groups = {OnCreate.class, OnUpdate.class})
    private Integer ageTo;

    private boolean withChildren;

    @NotNull(message = "{validation.not_null.photo}", groups = OnCreate.class)
    @Null(message = "{validation.null.photo}", groups = OnUpdate.class)
    private MultipartFile photo;

    @NotNull(message = "{validation.not_null.route}", groups = {OnCreate.class, OnUpdate.class})
    private String route;

    @Null(message = "{validation.null.events}", groups = OnUpdate.class)
    private String events;
}
