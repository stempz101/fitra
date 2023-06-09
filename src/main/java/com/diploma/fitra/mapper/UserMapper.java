package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.user.UserDto;
import com.diploma.fitra.dto.user.UserSaveDto;
import com.diploma.fitra.dto.user.UserShortDto;
import com.diploma.fitra.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fullName", target = "name")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "about", target = "about")
    UserDto toUserDto(User user);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "fullName", target = "name")
    UserShortDto toUserShortDto(User user);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(target = "fullName", expression = "java(userSaveDto.getFirstName() + \" \" + userSaveDto.getLastName())")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "about", target = "about")
    User fromUserSaveDto(UserSaveDto userSaveDto);
}
