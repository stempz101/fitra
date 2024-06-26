package com.diploma.fitra.model.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceReviewLikeKey implements Serializable {

    @Column
    Long reviewId;

    @Column
    Long userId;
}
