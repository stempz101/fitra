package com.diploma.fitra.model;

import com.diploma.fitra.model.key.ParticipantKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "_participant")
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
    private Travel travel;

    @JoinColumn(nullable = false)
    @ManyToOne
    @MapsId("userId")
    private User user;

    private Boolean isCreator;
}
