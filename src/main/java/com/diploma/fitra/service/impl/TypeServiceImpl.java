package com.diploma.fitra.service.impl;

import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.exception.ExistenceException;
import com.diploma.fitra.exception.ForbiddenException;
import com.diploma.fitra.exception.NotFoundException;
import com.diploma.fitra.mapper.TypeMapper;
import com.diploma.fitra.mapper.UpdateMapper;
import com.diploma.fitra.model.Type;
import com.diploma.fitra.model.error.Error;
import com.diploma.fitra.repo.TypeRepository;
import com.diploma.fitra.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TypeServiceImpl implements TypeService {
    private final TypeRepository typeRepository;

    @Override
    public Type createType(TypeSaveDto typeSaveDto, UserDetails userDetails) {
        log.info("Saving type: {}", typeSaveDto);

        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        } else if (typeRepository.existsByNameEn(typeSaveDto.getNameEn())) {
            throw new ExistenceException(Error.TYPE_EXISTS_WITH_NAME_EN.getMessage());
        } else if (typeRepository.existsByNameUa(typeSaveDto.getNameUa())) {
            throw new ExistenceException(Error.TYPE_EXISTS_WITH_NAME_UA.getMessage());
        }

        Type type = TypeMapper.INSTANCE.fromTypeSaveDto(typeSaveDto);

        type = typeRepository.save(type);
        log.info("Type is saved: {}", type);
        return type;
    }

    @Override
    public List<TypeDto> getTypes() {
        log.info("Getting types");

        return typeRepository.findAll().stream()
                .map(TypeMapper.INSTANCE::toTypeDto)
                .collect(Collectors.toList());
    }

    @Override
    public Type getType(Long typeId) {
        log.info("Getting type with id: {}", typeId);

        Type type = typeRepository.findById(typeId)
                .orElseThrow(() -> new NotFoundException(Error.TYPE_NOT_FOUND.getMessage()));

        log.info("Received category (id={}): {}", typeId, type);
        return type;
    }

    @Override
    public Type updateType(Long typeId, TypeSaveDto typeSaveDto, UserDetails userDetails) {
        log.info("Updating type (id={}): {}", typeId, typeSaveDto);

        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            throw new ForbiddenException(Error.ACCESS_DENIED.getMessage());
        }

        Type type = typeRepository.findById(typeId)
                .orElseThrow(() -> new NotFoundException(Error.TYPE_NOT_FOUND.getMessage()));

        if (typeRepository.existsByNameEn(typeSaveDto.getNameEn()) &&
                !type.getNameEn().equals(typeSaveDto.getNameEn())) {
            throw new ExistenceException(Error.TYPE_EXISTS_WITH_NAME_EN.getMessage());
        } else if (typeRepository.existsByNameUa(typeSaveDto.getNameUa()) &&
                !type.getNameUa().equals(typeSaveDto.getNameUa())) {
            throw new ExistenceException(Error.TYPE_EXISTS_WITH_NAME_UA.getMessage());
        }

        type = UpdateMapper.updateTypeWithPresentTypeSaveDtoFields(type, typeSaveDto);

        Type updatedType = typeRepository.save(type);
        log.info("Type (id={}) is updated: {}", typeId, updatedType);
        return updatedType;
    }
}
