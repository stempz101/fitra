package com.diploma.fitra.test.util;

import com.diploma.fitra.dto.travel.RouteDto;
import com.diploma.fitra.model.City;
import com.diploma.fitra.model.Country;
import com.diploma.fitra.model.Route;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.key.RouteKey;

public class RouteDataTest {

    private final static Travel ROUTE_TRAVEL = TravelDataTest.getTravel1();

    private final static Country ROUTE_1_COUNTRY = CountryDataTest.getCountry1();
    private final static City ROUTE_1_CITY = CityDataTest.getCity1();
    private final static int ROUTE_1_PRIORITY = 1;

    private final static Country ROUTE_2_COUNTRY = CountryDataTest.getCountry1();
    private final static City ROUTE_2_CITY = CityDataTest.getCity2();
    private final static int ROUTE_2_PRIORITY = 2;

    private final static Country ROUTE_3_COUNTRY = CountryDataTest.getCountry2();
    private final static City ROUTE_3_CITY = null;
    private final static int ROUTE_3_PRIORITY = 3;

    public static Route getRoute1() {
        Route route = new Route();
        route.setId(new RouteKey(ROUTE_TRAVEL.getId(), ROUTE_1_COUNTRY.getId(), ROUTE_1_CITY.getId()));
        route.setTravel(ROUTE_TRAVEL);
        route.setCountry(ROUTE_1_COUNTRY);
        route.setCity(ROUTE_1_CITY);
        route.setPriority(ROUTE_1_PRIORITY);
        return route;
    }

    public static Route getRoute2() {
        Route route = new Route();
        route.setId(new RouteKey(ROUTE_TRAVEL.getId(), ROUTE_2_COUNTRY.getId(), ROUTE_2_CITY.getId()));
        route.setTravel(ROUTE_TRAVEL);
        route.setCountry(ROUTE_2_COUNTRY);
        route.setCity(ROUTE_2_CITY);
        route.setPriority(ROUTE_2_PRIORITY);
        return route;
    }

    public static Route getRoute3() {
        Route route = new Route();
        route.setId(new RouteKey(ROUTE_TRAVEL.getId(), ROUTE_3_COUNTRY.getId(), null));
        route.setTravel(ROUTE_TRAVEL);
        route.setCountry(ROUTE_3_COUNTRY);
        route.setCity(ROUTE_3_CITY);
        route.setPriority(ROUTE_3_PRIORITY);
        return route;
    }

    public static RouteDto getRouteDto1() {
        RouteDto routeDto = createRouteDto1();
        routeDto.setCountry(ROUTE_1_COUNTRY.getTitleEn());
        routeDto.setCity(ROUTE_1_CITY.getTitleEn());
        return routeDto;
    }

    public static RouteDto getRouteDto2() {
        RouteDto routeDto = createRouteDto2();
        routeDto.setCountry(ROUTE_2_COUNTRY.getTitleEn());
        routeDto.setCity(ROUTE_2_CITY.getTitleEn());
        return routeDto;
    }

    public static RouteDto getRouteDto3() {
        RouteDto routeDto = createRouteDto3();
        routeDto.setCountry(ROUTE_3_COUNTRY.getTitleEn());
        routeDto.setCity(null);
        return routeDto;
    }

    public static RouteDto createRouteDto1() {
        RouteDto routeDto = new RouteDto();
        routeDto.setCountryId(ROUTE_1_COUNTRY.getId());
        routeDto.setCityId(ROUTE_1_CITY.getId());
        return routeDto;
    }

    public static RouteDto createRouteDto2() {
        RouteDto routeDto = new RouteDto();
        routeDto.setCountryId(ROUTE_2_COUNTRY.getId());
        routeDto.setCityId(ROUTE_2_CITY.getId());
        return routeDto;
    }

    public static RouteDto createRouteDto3() {
        RouteDto routeDto = new RouteDto();
        routeDto.setCountryId(ROUTE_3_COUNTRY.getId());
        routeDto.setCityId(null);
        return routeDto;
    }
}
