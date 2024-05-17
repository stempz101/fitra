package com.diploma.fitra.dto.travel;

import com.diploma.fitra.dto.user.UserShortDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipantDto {
    private UserShortDto user;
    private Boolean isCreator;
}
