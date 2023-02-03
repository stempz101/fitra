package com.diploma.fitra.service;

import com.diploma.fitra.dto.success.SuccessDto;
import com.diploma.fitra.dto.travel.ParticipantDto;
import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TravelService {

    TravelDto createTravel(TravelSaveDto travelSaveDto);

    List<TravelDto> getTravels();

    ResponseEntity<SuccessDto> addUser(Long travelId, Long userId);

    List<ParticipantDto> getUsers(Long travelId);

    void removeUser(Long travelId, Long userId);

    TravelDto updateTravel(Long travelId, TravelSaveDto travelSaveDto);

    void deleteTravel(Long travelId);
}
