package com.diploma.fitra.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserItemsResponse {
    private List<UserShortDto> items;
    private long totalItems;
}
