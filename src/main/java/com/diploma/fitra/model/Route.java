package com.diploma.fitra.model;

import com.diploma.fitra.model.key.RouteKey;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Travel travel;

    @JoinColumn(nullable = false)
    @ManyToOne
    @MapsId("countryId")
    private Country country;

    @ManyToOne
    @MapsId("cityId")
    private City city;

    @Column(nullable = false)
    private int position;
}
