package com.diploma.fitra.dto.travel;

import com.diploma.fitra.dto.group.OnCreate;
import com.diploma.fitra.dto.group.OnUpdate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteDto {

    @NotNull(message = "{validation.not_null.country_id}", groups = {OnCreate.class, OnUpdate.class})
    private Long countryId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String country;

    private long cityId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String city;

    @NotNull(message = "{validation.not_null.position}", groups = {OnCreate.class, OnUpdate.class})
    private Integer position;
}
