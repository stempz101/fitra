package com.diploma.fitra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity(name = "_image_travel")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ImageTravel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String path;

    private Boolean isMain;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Travel travel;
}
