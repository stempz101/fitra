package com.diploma.fitra.model;

import com.diploma.fitra.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity(name = "_create_requests")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CreateRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Travel travel;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime createTime;
}
