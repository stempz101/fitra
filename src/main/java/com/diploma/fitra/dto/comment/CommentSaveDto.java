package com.diploma.fitra.dto.comment;

import com.diploma.fitra.dto.group.IsNotUserComment;
import com.diploma.fitra.dto.group.IsUserComment;
import com.diploma.fitra.dto.validation.NoLink;
import com.diploma.fitra.dto.validation.NoPhoneNumber;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CommentSaveDto {

    @NotBlank(message = "{validation.not_blank.text}", groups = {IsUserComment.class, IsNotUserComment.class})
    @NoPhoneNumber(groups = {IsUserComment.class, IsNotUserComment.class})
    @NoLink(groups = {IsUserComment.class, IsNotUserComment.class})
    private String text;

    @NotNull(message = "{validation.not_null.rating}", groups = IsUserComment.class)
    @Min(value = 0, message = "{validation.min.rating}", groups = IsUserComment.class)
    @Max(value = 5, message = "{validation.max.rating}", groups = IsUserComment.class)
    @Null(message = "{validation.null.rating}", groups = IsNotUserComment.class)
    private Integer rating;
}
