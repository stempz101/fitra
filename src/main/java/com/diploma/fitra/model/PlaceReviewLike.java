package com.diploma.fitra.model;

import com.diploma.fitra.model.key.PlaceReviewLikeKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "_place_review_likes")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PlaceReviewLike {

    @EmbeddedId
    private PlaceReviewLikeKey id = new PlaceReviewLikeKey();

    @JoinColumn(nullable = false)
    @ManyToOne
    @MapsId("reviewId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PlaceReview placeReview;

    @JoinColumn(nullable = false)
    @ManyToOne
    @MapsId("userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
