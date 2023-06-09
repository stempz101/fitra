package com.diploma.fitra.repo.custom;

import com.diploma.fitra.model.Travel;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TravelCustomRepository {

    List<Travel> findAllByQueryParams(String name, Long countryId, Long cityId, Long typeId,
                                      LocalDate startDate, Integer peopleFrom, Integer peopleTo, Pageable pageable);

    long countByQueryParams(String name, Long countryId, Long cityId, Long typeId,
                            LocalDate startDate, Integer peopleFrom, Integer peopleTo);
}
