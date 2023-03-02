package com.diploma.fitra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "_travels")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Type type;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User creator;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private double budget;

    @Column(nullable = false)
    private int peopleLimit;

    @Column(nullable = false)
    private int ageFrom;

    @Column(nullable = false)
    private int ageTo;

    @Column(nullable = false)
    private boolean withChildren;

    @Column(nullable = false)
    private LocalDateTime createdTime;

    private boolean blocked;

}
