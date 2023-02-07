package com.diploma.fitra.model;

import com.diploma.fitra.model.enums.InvitationStatus;
import com.diploma.fitra.model.key.InvitationKey;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    @Column(nullable = false)
    private LocalDateTime createTime;
}
