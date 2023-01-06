package com.diploma.fitra.dto.travel;

import com.diploma.fitra.dto.user.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipantDto {
    private Long travelId;
    private UserDto user;
    private Boolean isCreator;
}
