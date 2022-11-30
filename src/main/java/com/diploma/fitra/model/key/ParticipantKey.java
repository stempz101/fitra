package com.diploma.fitra.model.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ParticipantKey implements Serializable {

    @Column
    Long travelId;

    @Column
    Long userId;
}
