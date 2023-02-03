package com.diploma.fitra.test.util;

import com.diploma.fitra.model.Invitation;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.key.InvitationKey;

public class InvitationDataTest {

    private static final Travel INVITATION_TRAVEL = TravelDataTest.getTravel1();
    private static final User INVITATION_USER = UserDataTest.getUser2();

    public static Invitation getInvitation() {
        Invitation invitation = new Invitation();
        invitation.setId(new InvitationKey(INVITATION_TRAVEL.getId(), INVITATION_USER.getId()));
        invitation.setTravel(INVITATION_TRAVEL);
        invitation.setUser(INVITATION_USER);
        return invitation;
    }
}
