package com.diploma.fitra.test.util;

import com.diploma.fitra.dto.travel.RouteDto;
import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import com.diploma.fitra.dto.travel.TravelShortDto;
import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.user.UserShortDto;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.Type;
import com.diploma.fitra.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TravelDataTest {

    private static final Long TRAVEL_1_ID = 1L;
    private static final String TRAVEL_1_TITLE = "Travel title 1";
    private static final LocalDate TRAVEL_1_START_DATE = LocalDate.of(2023, 4, 23);
    private static final Type TRAVEL_1_TYPE = TypeDataTest.getType1();
    private static final TypeDto TRAVEL_1_TYPE_DTO = TypeDataTest.getTypeDto1();
    private static final User TRAVEL_1_CREATOR = UserDataTest.getUser1();
    private static final UserShortDto TRAVEL_1_CREATOR_SHORT_DTO = UserDataTest.getUserShortDto1();

    private static final Long TRAVEL_2_ID = 2L;
    private static final String TRAVEL_2_TITLE = "Travel title 2";
    private static final LocalDate TRAVEL_2_START_DATE = LocalDate.of(2023, 1, 30);
    private static final Type TRAVEL_2_TYPE = TypeDataTest.getType4();
    private static final TypeDto TRAVEL_2_TYPE_DTO = TypeDataTest.getTypeDto4();
    private static final User TRAVEL_2_CREATOR = UserDataTest.getUser1();
    private static final UserShortDto TRAVEL_2_CREATOR_SHORT_DTO = UserDataTest.getUserShortDto1();

    private static final Long TRAVEL_3_ID = 3L;
    private static final String TRAVEL_3_TITLE = "Travel title 3";
    private static final LocalDate TRAVEL_3_START_DATE = LocalDate.of(2023, 7, 14);
    private static final Type TRAVEL_3_TYPE = TypeDataTest.getType2();
    private static final TypeDto TRAVEL_3_TYPE_DTO = TypeDataTest.getTypeDto2();
    private static final User TRAVEL_3_CREATOR = UserDataTest.getUser2();
    private static final UserShortDto TRAVEL_3_CREATOR_SHORT_DTO = UserDataTest.getUserShortDto2();

    private static final String TRAVEL_DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus, aspernatur atque consectetur distinctio dolorum enim error et fugit id incidunt ipsam neque nisi optio perferendis porro possimus provident quaerat qui ratione repudiandae similique tempore totam ut. Animi delectus distinctio harum ipsum laborum libero, minima mollitia nam, pariatur sint vero vitae.";
    private static final Integer TRAVEL_LIMIT = 10;
    private static final RouteDto TRAVEL_ROUTE_DTO_1 = RouteDataTest.getRouteDto1();
    private static final RouteDto TRAVEL_ROUTE_DTO_2 = RouteDataTest.getRouteDto2();
    private static final RouteDto TRAVEL_ROUTE_DTO_3 = RouteDataTest.getRouteDto3();
    private static final RouteDto TRAVEL_CREATE_ROUTE_DTO_1 = RouteDataTest.createRouteDto1();
    private static final RouteDto TRAVEL_CREATE_ROUTE_DTO_2 = RouteDataTest.createRouteDto2();
    private static final RouteDto TRAVEL_CREATE_ROUTE_DTO_3 = RouteDataTest.createRouteDto3();


    public static Travel getTravel1() {
        Travel travel = new Travel();
        travel.setId(TRAVEL_1_ID);
        travel.setTitle(TRAVEL_1_TITLE);
        travel.setDescription(TRAVEL_DESCRIPTION);
        travel.setPeopleLimit(TRAVEL_LIMIT);
        travel.setStartDate(TRAVEL_1_START_DATE);
        travel.setType(TRAVEL_1_TYPE);
        travel.setCreator(TRAVEL_1_CREATOR);
        return travel;
    }

    public static Travel getTravel2() {
        Travel travel = new Travel();
        travel.setId(TRAVEL_2_ID);
        travel.setTitle(TRAVEL_2_TITLE);
        travel.setDescription(TRAVEL_DESCRIPTION);
        travel.setPeopleLimit(TRAVEL_LIMIT);
        travel.setStartDate(TRAVEL_2_START_DATE);
        travel.setType(TRAVEL_2_TYPE);
        travel.setCreator(TRAVEL_2_CREATOR);
        return travel;
    }

    public static Travel getTravel3() {
        Travel travel = new Travel();
        travel.setId(TRAVEL_3_ID);
        travel.setTitle(TRAVEL_3_TITLE);
        travel.setDescription(TRAVEL_DESCRIPTION);
        travel.setPeopleLimit(TRAVEL_LIMIT);
        travel.setStartDate(TRAVEL_3_START_DATE);
        travel.setType(TRAVEL_3_TYPE);
        travel.setCreator(TRAVEL_3_CREATOR);
        return travel;
    }

    public static TravelDto getTravelDto1() {
        TravelDto travelDto = new TravelDto();

        List<RouteDto> routeDtoList = new ArrayList<>();
        routeDtoList.add(TRAVEL_ROUTE_DTO_1);
        routeDtoList.add(TRAVEL_ROUTE_DTO_2);
        routeDtoList.add(TRAVEL_ROUTE_DTO_3);

        travelDto.setId(TRAVEL_1_ID);
        travelDto.setTitle(TRAVEL_1_TITLE);
        travelDto.setType(TRAVEL_1_TYPE_DTO);
        travelDto.setDescription(TRAVEL_DESCRIPTION);
        travelDto.setLimit(TRAVEL_LIMIT);
        travelDto.setStartDate(TRAVEL_1_START_DATE);
        travelDto.setCreator(TRAVEL_1_CREATOR_SHORT_DTO);
        travelDto.setRoute(routeDtoList);

        return travelDto;
    }

    public static TravelDto getTravelDto2() {
        TravelDto travelDto = new TravelDto();

        List<RouteDto> routeDtoList = new ArrayList<>();
        routeDtoList.add(TRAVEL_ROUTE_DTO_1);
        routeDtoList.add(TRAVEL_ROUTE_DTO_2);
        routeDtoList.add(TRAVEL_ROUTE_DTO_3);

        travelDto.setId(TRAVEL_2_ID);
        travelDto.setTitle(TRAVEL_2_TITLE);
        travelDto.setType(TRAVEL_2_TYPE_DTO);
        travelDto.setDescription(TRAVEL_DESCRIPTION);
        travelDto.setLimit(TRAVEL_LIMIT);
        travelDto.setStartDate(TRAVEL_2_START_DATE);
        travelDto.setCreator(TRAVEL_2_CREATOR_SHORT_DTO);
        travelDto.setRoute(routeDtoList);

        return travelDto;
    }

    public static TravelDto getTravelDto3() {
        TravelDto travelDto = new TravelDto();

        List<RouteDto> routeDtoList = new ArrayList<>();
        routeDtoList.add(TRAVEL_ROUTE_DTO_1);
        routeDtoList.add(TRAVEL_ROUTE_DTO_2);
        routeDtoList.add(TRAVEL_ROUTE_DTO_3);

        travelDto.setId(TRAVEL_3_ID);
        travelDto.setTitle(TRAVEL_3_TITLE);
        travelDto.setType(TRAVEL_3_TYPE_DTO);
        travelDto.setDescription(TRAVEL_DESCRIPTION);
        travelDto.setLimit(TRAVEL_LIMIT);
        travelDto.setStartDate(TRAVEL_3_START_DATE);
        travelDto.setCreator(TRAVEL_3_CREATOR_SHORT_DTO);
        travelDto.setRoute(routeDtoList);

        return travelDto;
    }

    public static TravelSaveDto getTravelSaveDto1() {
        TravelSaveDto travelSaveDto = new TravelSaveDto();

        List<RouteDto> routeDtoList = new ArrayList<>();
        routeDtoList.add(TRAVEL_CREATE_ROUTE_DTO_1);
        routeDtoList.add(TRAVEL_CREATE_ROUTE_DTO_2);
        routeDtoList.add(TRAVEL_CREATE_ROUTE_DTO_3);

        travelSaveDto.setTitle(TRAVEL_1_TITLE);
        travelSaveDto.setTypeId(TRAVEL_1_TYPE.getId());
        travelSaveDto.setDescription(TRAVEL_DESCRIPTION);
        travelSaveDto.setLimit(TRAVEL_LIMIT);
        travelSaveDto.setStartDate(TRAVEL_1_START_DATE);
        travelSaveDto.setCreatorId(TRAVEL_1_CREATOR_SHORT_DTO.getId());
        travelSaveDto.setRoute(routeDtoList);

        return travelSaveDto;
    }

    public static TravelShortDto getTravelShortDto1() {
        TravelShortDto travelShortDto = new TravelShortDto();
        travelShortDto.setId(TRAVEL_1_ID);
        travelShortDto.setTitle(TRAVEL_1_TITLE);
        travelShortDto.setCreator(TRAVEL_1_CREATOR_SHORT_DTO);
        return travelShortDto;
    }
}
