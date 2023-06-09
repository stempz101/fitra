package com.diploma.fitra.repo.custom;

import com.diploma.fitra.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserCustomRepository {

    List<User> findAllByQueryParams(String name, Long countryId, Long cityId, Pageable pageable);

    long countByQueryParams(String name, Long countryId, Long cityId);
}
