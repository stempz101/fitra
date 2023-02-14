package com.diploma.fitra.test.util;

import com.diploma.fitra.dto.request.RequestDto;
import com.diploma.fitra.dto.travel.TravelShortDto;
import com.diploma.fitra.dto.user.UserShortDto;
import com.diploma.fitra.model.Request;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.enums.Status;

import java.time.LocalDateTime;

public class RequestDataTest {

    private static final Long REQUEST_1_ID = 1L;
    private static final Status REQUEST_1_STATUS = Status.REJECTED;
    private static final LocalDateTime REQUEST_1_CREATE_TIME = LocalDateTime.of(2023, 2, 8, 12, 55);

    private static final Long REQUEST_2_ID = 2L;
    private static final Status REQUEST_2_STATUS = Status.CONFIRMED;
    private static final LocalDateTime REQUEST_2_CREATE_TIME = LocalDateTime.of(2023, 2, 8, 17, 41);

    private static final Long REQUEST_3_ID = 3L;
    private static final Status REQUEST_3_STATUS = Status.REJECTED;
    private static final LocalDateTime REQUEST_3_CREATE_TIME = LocalDateTime.of(2023, 2, 9, 4, 14);

    private static final Long REQUEST_4_ID = 4L;
    private static final Status REQUEST_4_STATUS = Status.WAITING;
    private static final LocalDateTime REQUEST_4_CREATE_TIME = LocalDateTime.of(2023, 2, 9, 11, 23);

    private static final Travel REQUEST_TRAVEL = TravelDataTest.getTravel3();
    private static final TravelShortDto REQUEST_TRAVEL_SHORT_DTO = TravelDataTest.getTravelShortDto3();
    private static final User REQUEST_USER = UserDataTest.getUser3();
    private static final UserShortDto REQUEST_USER_SHORT_DTO = UserDataTest.getUserShortDto3();

    public static Request getRequest1() {
        Request request = new Request();
        request.setId(REQUEST_1_ID);
        request.setTravel(REQUEST_TRAVEL);
        request.setUser(REQUEST_USER);
        request.setStatus(REQUEST_1_STATUS);
        request.setCreateTime(REQUEST_1_CREATE_TIME);
        return request;
    }

    public static Request getRequest2() {
        Request request = new Request();
        request.setId(REQUEST_2_ID);
        request.setTravel(REQUEST_TRAVEL);
        request.setUser(REQUEST_USER);
        request.setStatus(REQUEST_2_STATUS);
        request.setCreateTime(REQUEST_2_CREATE_TIME);
        return request;
    }

    public static Request getRequest3() {
        Request request = new Request();
        request.setId(REQUEST_3_ID);
        request.setTravel(REQUEST_TRAVEL);
        request.setUser(REQUEST_USER);
        request.setStatus(REQUEST_3_STATUS);
        request.setCreateTime(REQUEST_3_CREATE_TIME);
        return request;
    }

    public static Request getRequest4() {
        Request request = new Request();
        request.setId(REQUEST_4_ID);
        request.setTravel(REQUEST_TRAVEL);
        request.setUser(REQUEST_USER);
        request.setStatus(REQUEST_4_STATUS);
        request.setCreateTime(REQUEST_4_CREATE_TIME);
        return request;
    }

    public static RequestDto getRequestDto1() {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(REQUEST_1_ID);
        requestDto.setTravel(REQUEST_TRAVEL_SHORT_DTO);
        requestDto.setSender(REQUEST_USER_SHORT_DTO);
        requestDto.setStatus(REQUEST_1_STATUS.name());
        requestDto.setCreateTime(REQUEST_1_CREATE_TIME);
        return requestDto;
    }

    public static RequestDto getRequestDto2() {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(REQUEST_2_ID);
        requestDto.setTravel(REQUEST_TRAVEL_SHORT_DTO);
        requestDto.setSender(REQUEST_USER_SHORT_DTO);
        requestDto.setStatus(REQUEST_2_STATUS.name());
        requestDto.setCreateTime(REQUEST_2_CREATE_TIME);
        return requestDto;
    }

    public static RequestDto getRequestDto3() {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(REQUEST_3_ID);
        requestDto.setTravel(REQUEST_TRAVEL_SHORT_DTO);
        requestDto.setSender(REQUEST_USER_SHORT_DTO);
        requestDto.setStatus(REQUEST_3_STATUS.name());
        requestDto.setCreateTime(REQUEST_3_CREATE_TIME);
        return requestDto;
    }

    public static RequestDto getRequestDto4() {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(REQUEST_4_ID);
        requestDto.setTravel(REQUEST_TRAVEL_SHORT_DTO);
        requestDto.setSender(REQUEST_USER_SHORT_DTO);
        requestDto.setStatus(REQUEST_4_STATUS.name());
        requestDto.setCreateTime(REQUEST_4_CREATE_TIME);
        return requestDto;
    }
}
