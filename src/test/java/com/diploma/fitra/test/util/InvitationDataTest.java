package com.diploma.fitra.test.util;

import com.diploma.fitra.dto.invitation.InvitationDto;
import com.diploma.fitra.dto.travel.TravelShortDto;
import com.diploma.fitra.dto.user.UserShortDto;
import com.diploma.fitra.model.Invitation;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.enums.Status;

import java.time.LocalDateTime;

public class InvitationDataTest {

    private static final Long INVITATION_1_ID = 1L;
    private static final Status INVITATION_1_STATUS = Status.REJECTED;
    private static final LocalDateTime INVITATION_1_CREATE_TIME = LocalDateTime.of(2023, 2, 8, 12, 55);

    private static final Long INVITATION_2_ID = 2L;
    private static final Status INVITATION_2_STATUS = Status.CONFIRMED;
    private static final LocalDateTime INVITATION_2_CREATE_TIME = LocalDateTime.of(2023, 2, 8, 17, 41);

    private static final Long INVITATION_3_ID = 3L;
    private static final Status INVITATION_3_STATUS = Status.REJECTED;
    private static final LocalDateTime INVITATION_3_CREATE_TIME = LocalDateTime.of(2023, 2, 9, 4, 14);

    private static final Long INVITATION_4_ID = 4L;
    private static final Status INVITATION_4_STATUS = Status.WAITING;
    private static final LocalDateTime INVITATION_4_CREATE_TIME = LocalDateTime.of(2023, 2, 9, 11, 23);

    private static final Travel INVITATION_TRAVEL = TravelDataTest.getTravel3();
    private static final TravelShortDto INVITATION_TRAVEL_SHORT_DTO = TravelDataTest.getTravelShortDto3();
    private static final User INVITATION_USER = UserDataTest.getUser3();
    private static final UserShortDto INVITATION_USER_SHORT_DTO = UserDataTest.getUserShortDto3();

    public static Invitation getInvitation1() {
        Invitation invitation = new Invitation();
        invitation.setId(INVITATION_1_ID);
        invitation.setTravel(INVITATION_TRAVEL);
        invitation.setUser(INVITATION_USER);
        invitation.setStatus(INVITATION_1_STATUS);
        invitation.setCreateTime(INVITATION_1_CREATE_TIME);
        return invitation;
    }

    public static Invitation getInvitation2() {
        Invitation invitation = new Invitation();
        invitation.setId(INVITATION_2_ID);
        invitation.setTravel(INVITATION_TRAVEL);
        invitation.setUser(INVITATION_USER);
        invitation.setStatus(INVITATION_2_STATUS);
        invitation.setCreateTime(INVITATION_2_CREATE_TIME);
        return invitation;
    }

    public static Invitation getInvitation3() {
        Invitation invitation = new Invitation();
        invitation.setId(INVITATION_3_ID);
        invitation.setTravel(INVITATION_TRAVEL);
        invitation.setUser(INVITATION_USER);
        invitation.setStatus(INVITATION_3_STATUS);
        invitation.setCreateTime(INVITATION_3_CREATE_TIME);
        return invitation;
    }

    public static Invitation getInvitation4() {
        Invitation invitation = new Invitation();
        invitation.setId(INVITATION_4_ID);
        invitation.setTravel(INVITATION_TRAVEL);
        invitation.setUser(INVITATION_USER);
        invitation.setStatus(INVITATION_4_STATUS);
        invitation.setCreateTime(INVITATION_4_CREATE_TIME);
        return invitation;
    }

    public static InvitationDto getInvitationDto1() {
        InvitationDto invitationDto = new InvitationDto();
        invitationDto.setId(INVITATION_1_ID);
        invitationDto.setTravel(INVITATION_TRAVEL_SHORT_DTO);
        invitationDto.setReceiver(INVITATION_USER_SHORT_DTO);
        invitationDto.setCreateTime(INVITATION_1_CREATE_TIME);
        invitationDto.setStatus(INVITATION_1_STATUS.name());
        return invitationDto;
    }

    public static InvitationDto getInvitationDto2() {
        InvitationDto invitationDto = new InvitationDto();
        invitationDto.setId(INVITATION_2_ID);
        invitationDto.setTravel(INVITATION_TRAVEL_SHORT_DTO);
        invitationDto.setReceiver(INVITATION_USER_SHORT_DTO);
        invitationDto.setCreateTime(INVITATION_2_CREATE_TIME);
        invitationDto.setStatus(INVITATION_2_STATUS.name());
        return invitationDto;
    }

    public static InvitationDto getInvitationDto3() {
        InvitationDto invitationDto = new InvitationDto();
        invitationDto.setId(INVITATION_3_ID);
        invitationDto.setTravel(INVITATION_TRAVEL_SHORT_DTO);
        invitationDto.setReceiver(INVITATION_USER_SHORT_DTO);
        invitationDto.setCreateTime(INVITATION_3_CREATE_TIME);
        invitationDto.setStatus(INVITATION_3_STATUS.name());
        return invitationDto;
    }

    public static InvitationDto getInvitationDto4() {
        InvitationDto invitationDto = new InvitationDto();
        invitationDto.setId(INVITATION_4_ID);
        invitationDto.setTravel(INVITATION_TRAVEL_SHORT_DTO);
        invitationDto.setReceiver(INVITATION_USER_SHORT_DTO);
        invitationDto.setCreateTime(INVITATION_4_CREATE_TIME);
        invitationDto.setStatus(INVITATION_4_STATUS.name());
        return invitationDto;
    }
}
