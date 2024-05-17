package com.diploma.fitra.dto.travel;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TravelItemsResponse {

    private List<TravelDto> items;
    private long totalItems;
}
