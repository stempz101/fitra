package com.diploma.fitra.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "_user_photos")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    private boolean avatar;

    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;
}
