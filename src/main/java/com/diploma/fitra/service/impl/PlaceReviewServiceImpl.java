package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.dto.comment.CommentSaveDto;
import com.diploma.fitra.dto.placereview.PlaceReviewDto;
import com.diploma.fitra.dto.placereview.PlaceReviewSaveDto;
import com.diploma.fitra.exception.BadRequestException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.CommentMapper;
import com.diploma.fitra.mapper.PlaceReviewMapper;
import com.diploma.fitra.mapper.UpdateMapper;
import com.diploma.fitra.model.PlaceReview;
import com.diploma.fitra.model.PlaceReviewComment;
import com.diploma.fitra.model.PlaceReviewLike;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.model.key.PlaceReviewLikeKey;
import com.diploma.fitra.repo.PlaceReviewCommentRepository;
import com.diploma.fitra.repo.PlaceReviewLikeRepository;
import com.diploma.fitra.repo.PlaceReviewRepository;
import com.diploma.fitra.repo.UserRepository;
import com.diploma.fitra.service.PlaceReviewService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
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
    private final PlaceReviewCommentRepository placeReviewCommentRepository;
    private final UserRepository userRepository;

    @Value("${google-places-api.key}")
    private String apiKey;

    @Override
    public PlaceReviewDto createPlaceReview(PlaceReviewSaveDto placeReviewSaveDto, UserDetails userDetails) {
        log.info("Creating place review");

        User user = userRepository.findById(placeReviewSaveDto.getAuthorId()).
                orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        if (!user.getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        JsonNode jsonNode = getPlaceDetails(placeReviewSaveDto);
        PlaceReview placeReview = placeReviewRepository.save(toPlaceReview(placeReviewSaveDto, user, jsonNode));

        log.info("Place review of the user (id={}) was created successfully", user.getId());
        return toPlaceReviewDto(placeReview);
    }

    @Override
    public List<PlaceReviewDto> getPlaceReviews(Pageable pageable, UserDetails userDetails) {
        log.info("Getting place reviews");

        return placeReviewRepository.findAll(pageable).stream()
                .map(placeReview -> userDetails == null ? toPlaceReviewDto(placeReview) :
                        toPlaceReviewDto(placeReview, userDetails))
                .collect(Collectors.toList());
    }

    @Override
    public PlaceReviewDto getPlaceReview(Long reviewId, UserDetails userDetails) {
        log.info("Getting place review (id={})", reviewId);

        PlaceReview placeReview = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Error.PLACE_NOT_FOUND.getMessage()));

        return userDetails == null ? toPlaceReviewDto(placeReview) :
                toPlaceReviewDto(placeReview, userDetails);
    }

    @Override
    public void setLike(Long reviewId, UserDetails userDetails) {
        log.info("Setting like to the place review (id={})", reviewId);

        PlaceReview placeReview = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Error.PLACE_NOT_FOUND.getMessage()));
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));
        Optional<PlaceReviewLike> optPlaceReviewLike = placeReviewLikeRepository
                .findById(new PlaceReviewLikeKey(placeReview.getId(), user.getId()));

        PlaceReviewLike placeReviewLike;
        if (optPlaceReviewLike.isPresent()) {
            placeReviewLike = optPlaceReviewLike.get();
            placeReviewLikeRepository.delete(placeReviewLike);

            log.info("Place review (id={}) is unliked by user (id={})", reviewId, user.getId());
        } else {
            placeReviewLike = setPlaceReviewLike(placeReview, user);
            placeReviewLikeRepository.save(placeReviewLike);

            log.info("Place review (id={}) is liked by user (id={})", reviewId, user.getId());
        }
    }

    @Override
    public CommentDto createComment(CommentSaveDto commentSaveDto, Long reviewId, UserDetails userDetails) {
        log.info("Commenting review (id={})", reviewId);

        PlaceReview placeReview = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Error.PLACE_REVIEW_NOT_FOUND.getMessage()));
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));

        PlaceReviewComment comment = placeReviewCommentRepository
                .save(toPlaceReviewComment(commentSaveDto, placeReview, user));

        log.info("Comment (id={}) is created to the place review (id={}) by the user (id={})",
                comment.getId(), placeReview.getId(), user.getId());
        return CommentMapper.INSTANCE.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getComments(Long reviewId, Pageable pageable) {
        log.info("Getting comments for place review (id={})", reviewId);

        return placeReviewCommentRepository.findAllByPlaceReviewIdAndParentCommentIsNullOrderByCreateDateAsc(reviewId, pageable)
                .stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createReply(CommentSaveDto commentSaveDto, Long reviewId, Long commentId, UserDetails userDetails) {
        log.info("Replying to the comment (id={}) for the place review (id={})", commentId, reviewId);

        PlaceReview placeReview = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Error.PLACE_REVIEW_NOT_FOUND.getMessage()));
        PlaceReviewComment comment = placeReviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Error.COMMENT_NOT_FOUND.getMessage()));
        if (!comment.getPlaceReview().getId().equals(placeReview.getId())) {
            throw new BadRequestException(Error.COMMENT_IS_NOT_FOR_SPECIFIED_REVIEW.getMessage());
        }
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));

        PlaceReviewComment commentReply = placeReviewCommentRepository
                .save(toPlaceReviewComment(commentSaveDto, placeReview, user, comment));

        log.info("Reply (id={}) to the comment (id={}) for the place review (id={}) " +
                        "is created successfully by the user (id={})", commentReply.getId(), comment.getId(),
                placeReview.getId(), user.getId());
        return CommentMapper.INSTANCE.toCommentDto(commentReply);
    }

    @Override
    public List<CommentDto> getReplies(Long reviewId, Long commentId, Pageable pageable) {
        log.info("Getting replies for the comment (id={}) in place review (id={})", commentId, reviewId);

        return placeReviewCommentRepository.findAllByPlaceReviewIdAndParentCommentIdOrderByCreateDateAsc(reviewId, commentId, pageable)
                .stream()
                .map(CommentMapper.INSTANCE::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long reviewId, Long commentId, UserDetails userDetails) {
        log.info("Deleting the comment (id={}) in place review (id={})", commentId, reviewId);

        PlaceReview placeReview = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Error.PLACE_NOT_FOUND.getMessage()));
        PlaceReviewComment comment = placeReviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Error.COMMENT_NOT_FOUND.getMessage()));

        if (!comment.getPlaceReview().getId().equals(placeReview.getId())) {
            throw new BadRequestException(Error.COMMENT_IS_NOT_FOR_SPECIFIED_REVIEW.getMessage());
        } else if (!comment.getUser().getEmail().equals(userDetails.getUsername())) {
            if (!placeReview.getAuthor().getEmail().equals(userDetails.getUsername())) {
                throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
            }
        }

        placeReviewCommentRepository.delete(comment);
        log.info("The comment (id={}) is deleted successfully from place review (id={})", commentId, reviewId);
    }

    @Override
    public PlaceReviewDto updatePlaceReview(PlaceReviewSaveDto placeReviewSaveDto, Long reviewId, UserDetails userDetails) {
        log.info("Updating place review (id={})", reviewId);

        PlaceReview placeReview = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Error.PLACE_NOT_FOUND.getMessage()));
        if (!placeReview.getAuthor().getEmail().equals(userDetails.getUsername())) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        placeReview = UpdateMapper.updatePlaceReviewWithPresentPlaceReviewSaveDtoFields(placeReview, placeReviewSaveDto);
        if (!placeReview.isEdited()) {
            placeReview.setEdited(true);
        }
        placeReview = placeReviewRepository.save(placeReview);

        log.info("Place review of the user (id={}) was edited successfully", placeReview.getAuthor().getId());
        return toPlaceReviewDto(placeReview);
    }

    @Override
    public void deletePlaceReview(Long reviewId, UserDetails userDetails) {
        log.info("Deleting place review (id={})", reviewId);

        PlaceReview placeReview = placeReviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException(Error.PLACE_NOT_FOUND.getMessage()));
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

    private PlaceReviewDto toPlaceReviewDto(PlaceReview placeReview) {
        PlaceReviewDto placeReviewDto = PlaceReviewMapper.INSTANCE.toPlaceReviewDto(placeReview);
        placeReviewDto.setLikes(placeReviewLikeRepository.countByPlaceReview(placeReview));

        return placeReviewDto;
    }

    private PlaceReviewDto toPlaceReviewDto(PlaceReview placeReview, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException(Error.USER_NOT_FOUND.getMessage()));

        PlaceReviewDto placeReviewDto = PlaceReviewMapper.INSTANCE.toPlaceReviewDto(placeReview);
        placeReviewDto.setLikes(placeReviewLikeRepository.countByPlaceReview(placeReview));
        placeReviewDto.setLiked(placeReviewLikeRepository.existsByPlaceReviewAndUser(placeReview, user));

        return placeReviewDto;
    }

    private CommentDto toCommentDto(PlaceReviewComment comment) {
        CommentDto commentDto = CommentMapper.INSTANCE.toCommentDto(comment);
        commentDto.setReplies(placeReviewCommentRepository.countByParentCommentId(comment.getId()));

        return commentDto;
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

    private PlaceReviewLike setPlaceReviewLike(PlaceReview placeReview, User user) {
        PlaceReviewLike placeReviewLike = new PlaceReviewLike();
        placeReviewLike.setPlaceReview(placeReview);
        placeReviewLike.setUser(user);
        return placeReviewLike;
    }

    private PlaceReviewComment toPlaceReviewComment(CommentSaveDto commentSaveDto, PlaceReview placeReview, User user) {
        PlaceReviewComment comment = new PlaceReviewComment();
        comment.setPlaceReview(placeReview);
        comment.setUser(user);
        comment.setText(commentSaveDto.getText());
        comment.setCreateDate(LocalDateTime.now());
        return comment;
    }

    private PlaceReviewComment toPlaceReviewComment(CommentSaveDto commentSaveDto, PlaceReview placeReview,
                                                    User user, PlaceReviewComment parentComment) {
        PlaceReviewComment comment = toPlaceReviewComment(commentSaveDto, placeReview, user);
        comment.setParentComment(parentComment);
        return comment;
    }
}
