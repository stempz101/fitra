package com.diploma.fitra.model;

import com.diploma.fitra.model.key.InvitationKey;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "_invitation")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Invitation {

    @EmbeddedId
    private InvitationKey id = new InvitationKey();

    @JoinColumn(nullable = false)
    @ManyToOne
    @MapsId("travelId")
    private Travel travel;

    @JoinColumn(nullable = false)
    @ManyToOne
    @MapsId("userId")
    private User user;
}
