package com.diploma.fitra.test.util;

import com.diploma.fitra.model.Participant;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.key.ParticipantKey;

public class ParticipantDataTest {

    private static final Travel PARTICIPANT_1_TRAVEL = TravelDataTest.getTravel1();
    private static final User PARTICIPANT_1_USER = UserDataTest.getUser1();
    private static final boolean PARTICIPANT_1_IS_CREATOR = true;

    public static Participant getParticipant1() {
        Participant participant = new Participant();
        participant.setId(new ParticipantKey(PARTICIPANT_1_TRAVEL.getId(), PARTICIPANT_1_USER.getId()));
        participant.setTravel(PARTICIPANT_1_TRAVEL);
        participant.setUser(PARTICIPANT_1_USER);
        participant.setIsCreator(PARTICIPANT_1_IS_CREATOR);
        return participant;
    }
}
