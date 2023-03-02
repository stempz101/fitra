package com.diploma.fitra.dto.travel;

import com.diploma.fitra.dto.group.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class RouteSaveDto {

    @NotNull(message = "{validation.not_null.route}", groups = OnUpdate.class)
    private List<RouteDto> route;
}
