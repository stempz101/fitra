package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.placereview.PlaceReviewDto;
import com.diploma.fitra.dto.placereview.PlaceReviewSaveDto;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.PlaceReviewMapper;
import com.diploma.fitra.mapper.UpdateMapper;
import com.diploma.fitra.model.PlaceReview;
import com.diploma.fitra.model.PlaceReviewLike;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.model.key.PlaceReviewLikeKey;
import com.diploma.fitra.repo.PlaceReviewLikeRepository;
import com.diploma.fitra.repo.PlaceReviewRepository;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.service.PlaceReviewService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceReviewServiceImpl implements PlaceReviewService {

    private static final String GOOGLE_PLACE_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json";

    private final PlaceReviewRepository placeReviewRepository;
    private final PlaceReviewLikeRepository placeReviewLikeRepository;
    private final UserRepository userRepository;

    @Value("${google-places-api.key}")
    private String apiKey;

    @Override
    public PlaceReviewDto createPlaceReview(PlaceReviewSaveDto placeReviewSaveDto, Authentication auth) {
        log.info("Creating place review");

        User user = userRepository.findById(placeReviewSaveDto.getAuthorId()).
                orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!user.getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        JsonNode jsonNode = getPlaceDetails(placeReviewSaveDto);
        PlaceReview placeReview = placeReviewRepository.save(toPlaceReview(placeReviewSaveDto, user, jsonNode));

        log.info("Place review of the user (id={}) was created successfully", user.getId());
        return PlaceReviewMapper.INSTANCE.toPlaceReviewDto(placeReview);
    }

    @Override
    public List<PlaceReviewDto> getPlaceReviews(Pageable pageable) {
        log.info("Getting place reviews");

        return placeReviewRepository.findAll(pageable).stream()
                .map(PlaceReviewMapper.INSTANCE::toPlaceReviewDto)
                .collect(Collectors.toList());
    }

    @Override
    public PlaceReviewDto getPlaceReview(Long reviewId) {
        log.info("Getting place review (id={})", reviewId);

        PlaceReview placeReview = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Error.PLACE_NOT_FOUND.getMessage()));

        return PlaceReviewMapper.INSTANCE.toPlaceReviewDto(placeReview);
    }

    @Override
    @Transactional
    public void setLike(Long reviewId, Authentication auth) {
        log.info("Setting like to the place review (id={})", reviewId);

        PlaceReview placeReview = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Error.PLACE_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        Optional<PlaceReviewLike> optPlaceReviewLike = placeReviewLikeRepository
                .findById(new PlaceReviewLikeKey(placeReview.getId(), user.getId()));

        PlaceReviewLike placeReviewLike;
        if (optPlaceReviewLike.isPresent()) {
            placeReviewLike = optPlaceReviewLike.get();
            if (!placeReviewLike.isLike()) {
                placeReview.setDislikes(placeReview.getDislikes() - 1);
                placeReview.setLikes(placeReview.getLikes() + 1);

                placeReviewLike.setLike(true);

                placeReviewRepository.save(placeReview);
                placeReviewLikeRepository.save(placeReviewLike);

                log.info("Like (value={}) is set to the place review (id={})", true, reviewId);
            } else {
                placeReview.setLikes(placeReview.getLikes() - 1);

                placeReviewRepository.save(placeReview);
                placeReviewLikeRepository.delete(placeReviewLike);

                log.info("Like (value={}) is set to the place review (id={})", false, reviewId);
            }
        } else {
            placeReviewLike = setPlaceReviewLike(placeReview, user, true);

            placeReview.setLikes(placeReview.getLikes() + 1);

            placeReviewRepository.save(placeReview);
            placeReviewLikeRepository.save(placeReviewLike);

            log.info("Like (value={}) is set to the place review (id={})", true, reviewId);
        }
    }

    @Override
    @Transactional
    public void setDislike(Long reviewId, Authentication auth) {
        log.info("Setting dislike to the place review (id={})", reviewId);

        PlaceReview placeReview = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Error.PLACE_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        Optional<PlaceReviewLike> optPlaceReviewLike = placeReviewLikeRepository
                .findById(new PlaceReviewLikeKey(placeReview.getId(), user.getId()));

        PlaceReviewLike placeReviewLike;
        if (optPlaceReviewLike.isPresent()) {
            placeReviewLike = optPlaceReviewLike.get();
            if (placeReviewLike.isLike()) {
                placeReview.setLikes(placeReview.getLikes() - 1);
                placeReview.setDislikes(placeReview.getDislikes() + 1);

                placeReviewLike.setLike(false);

                placeReviewRepository.save(placeReview);
                placeReviewLikeRepository.save(placeReviewLike);

                log.info("Dislike (value={}) is set to the place review (id={})", true, reviewId);
            } else {
                placeReview.setDislikes(placeReview.getDislikes() - 1);

                placeReviewRepository.save(placeReview);
                placeReviewLikeRepository.delete(placeReviewLike);

                log.info("Dislike (value={}) is set to the place review (id={})", false, reviewId);
            }
        } else {
            placeReviewLike = setPlaceReviewLike(placeReview, user, false);

            placeReview.setDislikes(placeReview.getDislikes() + 1);

            placeReviewRepository.save(placeReview);
            placeReviewLikeRepository.save(placeReviewLike);

            log.info("Dislike (value={}) is set to the place review (id={})", true, reviewId);
        }
    }

    @Override
    public PlaceReviewDto updatePlaceReview(PlaceReviewSaveDto placeReviewSaveDto, Long reviewId, Authentication auth) {
        log.info("Updating place review (id={})", reviewId);

        PlaceReview placeReview = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Error.PLACE_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!placeReview.getAuthor().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        placeReview = UpdateMapper.updatePlaceReviewWithPresentPlaceReviewSaveDtoFields(placeReview, placeReviewSaveDto);
        if (!placeReview.isEdited()) {
            placeReview.setEdited(true);
        }
        placeReview = placeReviewRepository.save(placeReview);

        log.info("Place review of the user (id={}) was edited successfully", placeReview.getAuthor().getId());
        return PlaceReviewMapper.INSTANCE.toPlaceReviewDto(placeReview);
    }

    @Override
    public void deletePlaceReview(Long reviewId, Authentication auth) {
        log.info("Deleting place review (id={})", reviewId);

        PlaceReview placeReview = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Error.PLACE_NOT_FOUND.getMessage()));
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        if (!placeReview.getAuthor().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        placeReviewRepository.delete(placeReview);

        log.info("Place review (id={}) is deleted successfully", reviewId);
    }

    private JsonNode getPlaceDetails(PlaceReviewSaveDto placeReviewSaveDto) {
        JsonNode jsonNode = WebClient.create(GOOGLE_PLACE_DETAILS_URL)
                .get()
                .uri(uriBuilder -> uriBuilder.queryParam("place_id", placeReviewSaveDto.getPlaceId())
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        if (jsonNode == null) {
            throw new NotFoundException(Error.PLACE_NOT_FOUND.getMessage());
        }
        return jsonNode;
    }

    private static PlaceReview toPlaceReview(PlaceReviewSaveDto placeReviewSaveDto, User user, JsonNode jsonNode) {
        PlaceReview placeReview = PlaceReviewMapper.INSTANCE.fromPlaceReviewSaveDto(placeReviewSaveDto);
        placeReview.setPlaceName(jsonNode.get("result").get("name").asText());
        placeReview.setPlaceAddress(jsonNode.get("result").get("formatted_address").asText());
        if (jsonNode.get("result").get("photos") != null) {
            placeReview.setPlacePhotoReference(jsonNode.get("result").get("photos").get(0).get("photo_reference").asText());
        }
        placeReview.setAuthor(user);
        placeReview.setCreateDate(LocalDateTime.now());
        return placeReview;
    }

    private PlaceReviewLike setPlaceReviewLike(PlaceReview placeReview, User user, boolean isLike) {
        PlaceReviewLike placeReviewLike = new PlaceReviewLike();
        placeReviewLike.setPlaceReview(placeReview);
        placeReviewLike.setUser(user);
        placeReviewLike.setLike(isLike);
        return placeReviewLike;
    }
}
