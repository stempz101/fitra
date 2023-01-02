package com.diploma.fitra.dto.travel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteDto {

    @NotNull(message = "{validation.not_null.country_id}")
    private Long countryId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String country;

    private Long cityId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String city;
}
