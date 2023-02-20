package com.diploma.fitra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

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
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer peopleLimit;

    @Column(nullable = false)
    private LocalDate startDate;

    private boolean isConfirmed;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Type type;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User creator;
}
