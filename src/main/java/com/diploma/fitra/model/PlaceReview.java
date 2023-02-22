package com.diploma.fitra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity(name = "_place_reviews")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class PlaceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String placeId;

    @Column(nullable = false)
    private String placeName;

    @Column(nullable = false)
    private String placeAddress;

    private String placePhotoReference;

    @Column(nullable = false)
    private String review;

    @JoinColumn(nullable = false)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    private double rating;

    private long likes;

    private long dislikes;

    private boolean isEdited;

    @Column(nullable = false)
    private LocalDateTime createDate;
}
