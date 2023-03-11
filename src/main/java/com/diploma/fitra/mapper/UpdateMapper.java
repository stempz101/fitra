package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.placereview.PlaceReviewSaveDto;
import com.diploma.fitra.dto.travel.EventSaveDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.dto.user.UserInfoSaveDto;
import com.diploma.fitra.model.*;

public interface UpdateMapper {

    static Travel updateTravelWithPresentTravelSaveDtoFields(Travel travel, TravelSaveDto travelSaveDto) {
        if (travel == null)
            return null;
        else if (travelSaveDto == null)
            return travel;

        travel.setName(travelSaveDto.getName());
        travel.setDescription(travelSaveDto.getDescription());
        travel.setStartDate(travelSaveDto.getStartDate());
        travel.setEndDate(travelSaveDto.getEndDate());
        travel.setBudget(travelSaveDto.getBudget());
        travel.setPeopleLimit(travelSaveDto.getLimit());
        travel.setAgeFrom(travelSaveDto.getAgeFrom());
        travel.setAgeTo(travelSaveDto.getAgeTo());
        travel.setWithChildren(travelSaveDto.isWithChildren());

        return travel;
    }

    static Event updateEventWithPresentEventSaveDtoFields(Event event, EventSaveDto eventSaveDto) {
        if (event == null)
            return null;
        else if (eventSaveDto == null)
            return event;

        event.setName(eventSaveDto.getName());
        event.setStartTime(eventSaveDto.getStartTime());
        event.setEndTime(eventSaveDto.getEndTime());

        return event;
    }

    static Type updateTypeWithPresentTypeSaveDtoFields(Type type, TypeSaveDto typeSaveDto) {
        if (type == null)
            return null;
        else if (typeSaveDto == null)
            return type;

        type.setNameEn(typeSaveDto.getNameEn());
        type.setNameUa(typeSaveDto.getNameUa());

        return type;
    }

    static PlaceReview updatePlaceReviewWithPresentPlaceReviewSaveDtoFields(PlaceReview placeReview, PlaceReviewSaveDto placeReviewSaveDto) {
        if (placeReview == null)
            return null;
        else if (placeReviewSaveDto == null)
            return placeReview;

        placeReview.setTitle(placeReviewSaveDto.getTitle());
        placeReview.setReview(placeReviewSaveDto.getReview());
        placeReview.setRating(placeReviewSaveDto.getRating());

        return placeReview;
    }

    static User updateUserWithPresentUserInfoSaveDtoFields(User user, UserInfoSaveDto userInfoSaveDto) {
        if (user == null)
            return null;
        else if (userInfoSaveDto == null)
            return user;

        user.setFirstName(userInfoSaveDto.getFirstName());
        user.setLastName(userInfoSaveDto.getLastName());
        user.setBirthday(userInfoSaveDto.getBirthday());

        return user;
    }
}
