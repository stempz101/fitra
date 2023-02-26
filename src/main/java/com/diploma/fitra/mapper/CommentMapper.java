package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.comment.CommentDto;
import com.diploma.fitra.model.PlaceReviewComment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    CommentDto toCommentDto(PlaceReviewComment comment);
}
