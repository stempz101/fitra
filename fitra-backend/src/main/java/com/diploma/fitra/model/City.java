package com.diploma.fitra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "_cities")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titleEn;

    @Column(nullable = false)
    private String titleUa;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Country country;
}
