package com.diploma.fitra.test.util;

import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.user.UserShortDto;
import com.diploma.fitra.model.Participant;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.key.ParticipantKey;

public class ParticipantDataTest {
    private static final User PARTICIPANT_1_USER = UserDataTest.getUser1();
    private static final UserShortDto PARTICIPANT_1_USER_SHORT_DTO = UserDataTest.getUserShortDto1();

    private static final User PARTICIPANT_2_USER = UserDataTest.getUser2();
    private static final UserShortDto PARTICIPANT_2_USER_SHORT_DTO = UserDataTest.getUserShortDto2();

    private static final User PARTICIPANT_3_USER = UserDataTest.getUser3();
    private static final UserShortDto PARTICIPANT_3_USER_SHORT_DTO = UserDataTest.getUserShortDto3();

    private static final Travel PARTICIPANT_TRAVEL = TravelDataTest.getTravel1();
    private static final boolean PARTICIPANT_IS_CREATOR = true;
    private static final boolean PARTICIPANT_IS_NOT_CREATOR = false;

    public static Participant getParticipant1() {
        Participant participant = new Participant();
        participant.setId(new ParticipantKey(PARTICIPANT_TRAVEL.getId(), PARTICIPANT_1_USER.getId()));
        participant.setTravel(PARTICIPANT_TRAVEL);
        participant.setUser(PARTICIPANT_1_USER);
        participant.setCreator(PARTICIPANT_IS_CREATOR);
        return participant;
    }

    public static Participant getParticipant2() {
        Participant participant = new Participant();
        participant.setId(new ParticipantKey(PARTICIPANT_TRAVEL.getId(), PARTICIPANT_2_USER.getId()));
        participant.setTravel(PARTICIPANT_TRAVEL);
        participant.setUser(PARTICIPANT_2_USER);
        participant.setCreator(PARTICIPANT_IS_NOT_CREATOR);
        return participant;
    }

    public static Participant getParticipant3() {
        Participant participant = new Participant();
        participant.setId(new ParticipantKey(PARTICIPANT_TRAVEL.getId(), PARTICIPANT_3_USER.getId()));
        participant.setTravel(PARTICIPANT_TRAVEL);
        participant.setUser(PARTICIPANT_3_USER);
        participant.setCreator(PARTICIPANT_IS_NOT_CREATOR);
        return participant;
    }

    public static ParticipantDto getParticipantDto1() {
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setUser(PARTICIPANT_1_USER_SHORT_DTO);
        participantDto.setIsCreator(PARTICIPANT_IS_CREATOR);
        return participantDto;
    }

    public static ParticipantDto getParticipantDto2() {
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setUser(PARTICIPANT_2_USER_SHORT_DTO);
        return participantDto;
    }

    public static ParticipantDto getParticipantDto3() {
        ParticipantDto participantDto = new ParticipantDto();
        participantDto.setUser(PARTICIPANT_3_USER_SHORT_DTO);
        return participantDto;
    }
}
