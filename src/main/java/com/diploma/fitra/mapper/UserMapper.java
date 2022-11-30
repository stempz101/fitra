package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.user.UserSaveDto;
import com.diploma.fitra.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User fromUserSaveDto(UserSaveDto userDto);
}