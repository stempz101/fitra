package com.diploma.fitra.mapper;

import com.diploma.fitra.dto.user.UserSaveDto;
import com.diploma.fitra.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthenticationMapper {

    AuthenticationMapper INSTANCE = Mappers.getMapper(AuthenticationMapper.class);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "about", target = "about")
    User fromRegisterDto(UserSaveDto userSaveDto);
}
