package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.model.PlaceReviewComment;
import com.diploma.fitra.model.UserComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "user.fullName", target = "user.name")
    CommentDto toCommentDto(PlaceReviewComment comment);

    @Mapping(source = "author.fullName", target = "user.name")
    CommentDto toCommentDto(UserComment comment);
}
