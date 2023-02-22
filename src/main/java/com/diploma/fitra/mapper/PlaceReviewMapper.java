package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.placereview.PlaceReviewDto;
import com.diploma.fitra.dto.placereview.PlaceReviewSaveDto;
import com.diploma.fitra.model.PlaceReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlaceReviewMapper {

    PlaceReviewMapper INSTANCE = Mappers.getMapper(PlaceReviewMapper.class);

    PlaceReview fromPlaceReviewSaveDto(PlaceReviewSaveDto placeReviewSaveDto);

    @Mapping(source = "placeId", target = "place.id")
    @Mapping(source = "placeName", target = "place.name")
    @Mapping(source = "placeAddress", target = "place.address")
    @Mapping(source = "placePhotoReference", target = "place.photoReference")
    PlaceReviewDto toPlaceReviewDto(PlaceReview placeReview);
}
