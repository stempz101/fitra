package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.placereview.PlaceReviewSaveDto;
import com.diploma.fitra.dto.travel.TravelSaveDto;
import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.model.PlaceReview;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.Type;

public interface UpdateMapper {

    static Travel updateTravelWithPresentTravelSaveDtoFields(Travel travel, TravelSaveDto travelSaveDto) {
        if (travel == null)
            return null;

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

    static Type updateTypeWithPresentTypeSaveDtoFields(Type type, TypeSaveDto typeSaveDto) {
        if (type == null)
            return null;

        type.setNameEn(typeSaveDto.getNameEn());
        type.setNameUa(typeSaveDto.getNameUa());

        return type;
    }

    static PlaceReview updatePlaceReviewWithPresentPlaceReviewSaveDtoFields(PlaceReview placeReview, PlaceReviewSaveDto placeReviewSaveDto) {
        if (placeReview == null)
            return null;

        placeReview.setTitle(placeReviewSaveDto.getTitle());
        placeReview.setReview(placeReviewSaveDto.getReview());
        placeReview.setRating(placeReviewSaveDto.getRating());

        return placeReview;
    }
}
