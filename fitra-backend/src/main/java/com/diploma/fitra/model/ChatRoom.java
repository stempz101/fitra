package com.diploma.fitra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity(name = "_chat_rooms")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User user1;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User user2;
}
