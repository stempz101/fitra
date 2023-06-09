package com.diploma.fitra.dto.comment;

import com.diploma.fitra.dto.user.UserShortDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {

    private Long id;
    private UserShortDto user;
    private String text;
    private Integer rating;
    private LocalDateTime createDate;
}
