package com.diploma.fitra.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtDto {

    private String token;
}
