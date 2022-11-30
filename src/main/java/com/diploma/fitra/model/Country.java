package com.diploma.fitra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "_country")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private City city;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;
}
