package com.diploma.fitra.model.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteKey implements Serializable {

    @Column
    Long travelId;

    @Column
    Long countryId;

    @Column
    Long cityId;
}
