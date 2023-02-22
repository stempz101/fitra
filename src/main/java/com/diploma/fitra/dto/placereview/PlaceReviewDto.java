package com.diploma.fitra.dto.placereview;

import com.diploma.fitra.dto.user.UserShortDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlaceReviewDto {

    private Long id;
    private String title;
    private PlaceDto place;
    private String review;
    private UserShortDto author;
    private double rating;
    private long likes;
    private long dislikes;
    private boolean isEdited;
    private LocalDateTime createDate;
}
