package com.diploma.fitra.model;

import com.diploma.fitra.model.key.BlackListKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "_black_list")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BlackList {

    @EmbeddedId
    private BlackListKey id = new BlackListKey();

    @JoinColumn(nullable = false)
    @ManyToOne
    @MapsId("userId1")
    private User user1;

    @JoinColumn(nullable = false)
    @ManyToOne
    @MapsId("userId2")
    private User user2;
}
