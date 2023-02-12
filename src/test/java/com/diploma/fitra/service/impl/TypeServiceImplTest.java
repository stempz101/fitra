package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.TypeMapper;
import com.diploma.fitra.model.Type;
import com.diploma.fitra.model.User;
import com.diploma.fitra.repo.TypeRepository;
import com.diploma.fitra.test.util.TypeDataTest;
import com.diploma.fitra.test.util.UserDataTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TypeServiceImplTest {

    @InjectMocks
    private TypeServiceImpl typeService;

    @Mock
    private TypeRepository typeRepository;

    @Test
    void createTypeTest() {
        TypeSaveDto typeSaveDto = TypeDataTest.getTypeSaveDto1();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser1();
        Type type = TypeDataTest.getType1();

        when(auth.getPrincipal()).thenReturn(user);
        when(typeRepository.existsByNameEn(any())).thenReturn(false);
        when(typeRepository.existsByNameUa(any())).thenReturn(false);
        when(typeRepository.save(any())).thenReturn(type);
        Type result = typeService.createType(typeSaveDto, auth);

        assertThat(result, allOf(
                hasProperty("id", equalTo(type.getId())),
                hasProperty("nameEn", equalTo(typeSaveDto.getNameEn())),
                hasProperty("nameUa", equalTo(typeSaveDto.getNameUa()))
        ));
    }

    @Test
    void createTypeWithForbiddenExceptionTest() {
        TypeSaveDto typeSaveDto = TypeDataTest.getTypeSaveDto1();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser2();

        when(auth.getPrincipal()).thenReturn(user);

        assertThrows(ForbiddenException.class, () -> typeService.createType(typeSaveDto, auth));
    }

    @Test
    void createTypeWithNameEnExistenceExceptionTest() {
        TypeSaveDto typeSaveDto = TypeDataTest.getTypeSaveDto1();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser1();

        when(auth.getPrincipal()).thenReturn(user);
        when(typeRepository.existsByNameEn(typeSaveDto.getNameEn())).thenReturn(true);

        assertThrows(ExistenceException.class, () -> typeService.createType(typeSaveDto, auth));
    }

    @Test
    void createTypeWithNameUaExistenceExceptionTest() {
        TypeSaveDto typeSaveDto = TypeDataTest.getTypeSaveDto1();
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser1();

        when(auth.getPrincipal()).thenReturn(user);
        when(typeRepository.existsByNameEn(typeSaveDto.getNameEn())).thenReturn(false);
        when(typeRepository.existsByNameUa(typeSaveDto.getNameUa())).thenReturn(true);

        assertThrows(ExistenceException.class, () -> typeService.createType(typeSaveDto, auth));
    }

    @Test
    void getTypesTest() {
        List<Type> types = new ArrayList<>();
        types.add(TypeDataTest.getType1());
        types.add(TypeDataTest.getType2());
        types.add(TypeDataTest.getType3());
        types.add(TypeDataTest.getType4());

        when(typeRepository.findAll()).thenReturn(types);
        List<TypeDto> result = typeService.getTypes();

        assertThat(result, hasSize(types.size()));
        assertThat(result, hasItems(
                TypeDataTest.getTypeDto1(),
                TypeDataTest.getTypeDto2(),
                TypeDataTest.getTypeDto3(),
                TypeDataTest.getTypeDto4()
        ));
    }

    @Test
    void getTypeTest() {
        Type type = TypeDataTest.getType1();

        when(typeRepository.findById(any())).thenReturn(Optional.of(type));
        Type result = typeService.getType(type.getId());

        assertThat(result, allOf(
                hasProperty("id", equalTo(type.getId())),
                hasProperty("nameEn", equalTo(type.getNameEn())),
                hasProperty("nameUa", equalTo(type.getNameUa()))
        ));
    }

    @Test
    void getTypeWithNotFoundException() {
        Type type = TypeDataTest.getType1();

        when(typeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> typeService.getType(type.getId()));
    }

    @Test
    void updateTypeTest() {
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser1();
        Type type = TypeDataTest.getType2();
        TypeSaveDto typeSaveDto = TypeDataTest.getTypeSaveDto1();
        Type updatedType = TypeMapper.INSTANCE.fromTypeSaveDto(typeSaveDto);
        updatedType.setId(type.getId());

        when(auth.getPrincipal()).thenReturn(user);
        when(typeRepository.findById(any())).thenReturn(Optional.of(type));
        when(typeRepository.existsByNameEn(any())).thenReturn(false);
        when(typeRepository.existsByNameUa(any())).thenReturn(false);
        when(typeRepository.save(any())).thenReturn(updatedType);
        Type result = typeService.updateType(type.getId(), typeSaveDto, auth);

        assertThat(result, allOf(
                hasProperty("id", equalTo(type.getId())),
                hasProperty("nameEn", equalTo(typeSaveDto.getNameEn())),
                hasProperty("nameUa", equalTo(typeSaveDto.getNameUa()))
        ));
    }

    @Test
    void updateTypeWithForbiddenExceptionTest() {
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser2();
        Type type = TypeDataTest.getType2();
        TypeSaveDto typeSaveDto = TypeDataTest.getTypeSaveDto1();

        when(auth.getPrincipal()).thenReturn(user);

        assertThrows(ForbiddenException.class, () -> typeService.updateType(type.getId(), typeSaveDto, auth));
    }

    @Test
    void updateTypeWithNotFoundExceptionTest() {
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser1();
        Type type = TypeDataTest.getType2();
        TypeSaveDto typeSaveDto = TypeDataTest.getTypeSaveDto1();

        when(auth.getPrincipal()).thenReturn(user);
        when(typeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> typeService.updateType(type.getId(), typeSaveDto, auth));
    }

    @Test
    void updateTypeWithNameEnExistenceExceptionTest() {
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser1();
        Type type = TypeDataTest.getType2();
        TypeSaveDto typeSaveDto = TypeDataTest.getTypeSaveDto1();

        when(auth.getPrincipal()).thenReturn(user);
        when(typeRepository.findById(any())).thenReturn(Optional.of(type));
        when(typeRepository.existsByNameEn(any())).thenReturn(true);

        assertThrows(ExistenceException.class, () -> typeService.updateType(type.getId(), typeSaveDto, auth));
    }

    @Test
    void updateTypeWithNameUaExistenceExceptionTest() {
        Authentication auth = mock(Authentication.class);
        User user = UserDataTest.getUser1();
        Type type = TypeDataTest.getType2();
        TypeSaveDto typeSaveDto = TypeDataTest.getTypeSaveDto1();

        when(auth.getPrincipal()).thenReturn(user);
        when(typeRepository.findById(any())).thenReturn(Optional.of(type));
        when(typeRepository.existsByNameEn(any())).thenReturn(false);
        when(typeRepository.existsByNameUa(any())).thenReturn(true);

        assertThrows(ExistenceException.class, () -> typeService.updateType(type.getId(), typeSaveDto, auth));
    }
}
