package com.diploma.fitra.dto.comment;

import com.diploma.fitra.dto.validation.NoLink;
import com.diploma.fitra.dto.validation.NoPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentSaveDto {

    @NotBlank(message = "{validation.not_blank.text}")
    @NoPhoneNumber
    @NoLink
    private String text;
}
