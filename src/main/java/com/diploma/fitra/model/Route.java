package com.diploma.fitra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "_route")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Travel travel;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Country country;

    @JoinColumn
    @ManyToOne
    private City city;

    @Column(nullable = false)
    private int position;
}
