package com.diploma.fitra.api;

import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.model.Type;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/types")
public interface TypeApi {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    Type createType(@RequestBody @Valid TypeSaveDto typeSaveDto);

    @GetMapping
    List<TypeDto> getTypes();

    @GetMapping("/{typeId}")
    Type getType(@PathVariable Long typeId);

    @PutMapping("/{typeId}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    Type updateType(@PathVariable Long typeId, @RequestBody @Valid TypeSaveDto typeSaveDto);
}
