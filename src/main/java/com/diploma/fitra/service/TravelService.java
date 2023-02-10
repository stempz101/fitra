package com.diploma.fitra.service;

import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TravelService {

    TravelDto createTravel(TravelSaveDto travelSaveDto);

    List<TravelDto> getTravels();

    List<ParticipantDto> getUsers(Long travelId);

    void removeUser(Long travelId, Long userId);

    void leaveTravel(Long travelId, Long userId, Authentication auth);

    TravelDto updateTravel(Long travelId, TravelSaveDto travelSaveDto);

    void deleteTravel(Long travelId);
}
