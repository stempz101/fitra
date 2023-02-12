package com.diploma.fitra.service;

import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TravelService {

    TravelDto createTravel(TravelSaveDto travelSaveDto, Authentication auth);

    List<TravelDto> getTravels();

    List<ParticipantDto> getUsers(Long travelId);

    void removeUser(Long travelId, Long userId, Authentication auth);

    void leaveTravel(Long travelId, Long userId, Authentication auth);

    TravelDto updateTravel(Long travelId, TravelSaveDto travelSaveDto, Authentication auth);

    void deleteTravel(Long travelId, Authentication auth);
}
