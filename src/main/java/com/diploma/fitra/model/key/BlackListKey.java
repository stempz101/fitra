package com.diploma.fitra.model.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class BlackListKey implements Serializable {

    @Column
    Long userId1;

    @Column
    Long userId2;
}
