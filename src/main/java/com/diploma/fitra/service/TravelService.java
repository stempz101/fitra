package com.diploma.fitra.service;

import com.diploma.fitra.dto.travel.TravelDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;

import java.util.List;

public interface TravelService {

    TravelDto createTravel(TravelSaveDto travelSaveDto);

    List<TravelDto> getTravels();

    void deleteTravel(Long travelId);
}
