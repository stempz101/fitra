package com.diploma.fitra.dto.placereview;

import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.group.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
public class PlaceReviewSaveDto {

    @NotBlank(message = "{validation.not_blank.title}", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @NotBlank(message = "{validation.not_blank.place_id}", groups = OnCreate.class)
    @Null(message = "{validation.null.place_id}", groups = OnUpdate.class)
    private String placeId;

    @NotBlank(message = "{validation.not_blank.review}", groups = {OnCreate.class, OnUpdate.class})
    private String review;

    @NotNull(message = "{validation.not_null.author_id}", groups = OnCreate.class)
    @Null(message = "{validation.null.author_id}", groups = OnUpdate.class)
    private Long authorId;

    private double rating;
}
