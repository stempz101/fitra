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

        travel.setTitle(travelSaveDto.getTitle());
        travel.setDescription(travelSaveDto.getDescription());
        travel.setPeopleLimit(travelSaveDto.getLimit());
        travel.setStartDate(travelSaveDto.getStartDate());

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
