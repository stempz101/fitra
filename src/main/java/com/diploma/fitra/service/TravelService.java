package com.diploma.fitra.service;

import com.diploma.fitra.dto.travel.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TravelService {

    TravelDto createTravel(TravelSaveDto travelSaveDto, UserDetails userDetails);

    List<TravelDto> getTravels(Pageable pageable);

    List<TravelDto> getTravelsForUser(Pageable pageable, UserDetails userDetails);

    List<ParticipantDto> getUsers(Long travelId);

    void removeUser(Long travelId, Long userId, UserDetails userDetails);

    void leaveTravel(Long travelId, UserDetails userDetails);

    TravelDto updateTravel(Long travelId, TravelSaveDto travelSaveDto, UserDetails userDetails);

    List<RouteDto> updateRoute(Long travelId, RouteSaveDto routeSaveDto, UserDetails userDetails);

    void deleteTravel(Long travelId, UserDetails userDetails);
}
