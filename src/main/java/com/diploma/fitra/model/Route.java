package com.diploma.fitra.model;

import com.diploma.fitra.model.key.RouteKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "_route")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Route {

    @EmbeddedId
    private RouteKey id = new RouteKey();

    @JoinColumn(nullable = false)
    @ManyToOne
    @MapsId("travelId")
    private Travel travel;

    @JoinColumn(nullable = false)
    @ManyToOne
    @MapsId("countryId")
    private Country country;

    @ManyToOne
    @MapsId("cityId")
    private City city;

    @Column(nullable = false)
    private Integer priority;
}
