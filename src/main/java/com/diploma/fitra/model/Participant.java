package com.diploma.fitra.model;

import com.diploma.fitra.model.key.ParticipantKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "_participants")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Participant {

    @EmbeddedId
    private ParticipantKey id = new ParticipantKey();

    @JoinColumn(nullable = false)
    @ManyToOne
    @MapsId("travelId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Travel travel;

    @JoinColumn(nullable = false)
    @ManyToOne
    @MapsId("userId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private boolean isCreator;
}
