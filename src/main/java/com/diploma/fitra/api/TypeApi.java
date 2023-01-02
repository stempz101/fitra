package com.diploma.fitra.api;

import com.diploma.fitra.dto.type.TypeDto;
import com.diploma.fitra.dto.type.TypeSaveDto;
import com.diploma.fitra.model.Type;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/type")
public interface TypeApi {

    @PostMapping
    Type createType(@RequestBody @Valid TypeSaveDto typeSaveDto);

    @GetMapping
    List<TypeDto> getTypes();

    @GetMapping("/{typeId}")
    Type getType(@PathVariable Long typeId);

    @PutMapping("/{typeId}")
    Type updateType(@PathVariable Long typeId, @RequestBody @Valid TypeSaveDto typeSaveDto);
}
