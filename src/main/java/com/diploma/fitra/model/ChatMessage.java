package com.diploma.fitra.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "_chat_messages")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private ChatRoom room;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User sender;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User recipient;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean viewed;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}
