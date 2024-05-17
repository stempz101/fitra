package com.diploma.fitra.repo.custom.impl;

import com.diploma.fitra.model.Route;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.Type;
import com.diploma.fitra.repo.custom.TravelCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TravelCustomRepositoryImpl implements TravelCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<Travel> findAllByQueryParams(String name, Long countryId, Long cityId, Long typeId,
                                             LocalDate startDate, Integer peopleFrom, Integer peopleTo, Pageable pageable) {
        StringBuilder baseQuery = new StringBuilder()
                .append("SELECT t FROM _travels t ")
                .append("JOIN _routes r ON t.id = r.travel.id ")
                .append("JOIN _types tp ON t.type.id = tp.id ")
                .append("WHERE 1=1");
        buildJpqlQueryWithParams(name, countryId, cityId, typeId, startDate, peopleFrom, peopleTo, baseQuery);
        baseQuery.append(" GROUP BY t.id");
        baseQuery.append(" ORDER BY t.createdTime DESC");

        TypedQuery<Travel> query = entityManager.createQuery(baseQuery.toString(), Travel.class);
        setParamsToQuery(name, countryId, cityId, typeId, startDate, peopleFrom, peopleTo, query);
        if (pageable != null) {
            if (pageable.getPageNumber() <= 1) {
                query.setFirstResult(0);
            } else {
                query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
            }
            query.setMaxResults(pageable.getPageSize());
        }

        return query.getResultList();
    }

    @Override
    public long countByQueryParams(String name, Long countryId, Long cityId, Long typeId,
                                      LocalDate startDate, Integer peopleFrom, Integer peopleTo) {
        StringBuilder baseQuery = new StringBuilder()
                .append("SELECT COUNT(DISTINCT t.id) FROM _travels t ")
                .append("JOIN _routes r ON t.id = r.travel.id ")
                .append("JOIN _types tp ON t.type.id = tp.id ")
                .append("WHERE 1=1");
        buildJpqlQueryWithParams(name, countryId, cityId, typeId, startDate, peopleFrom, peopleTo, baseQuery);

        TypedQuery<Long> query = entityManager.createQuery(baseQuery.toString(), Long.class);
        setParamsToQuery(name, countryId, cityId, typeId, startDate, peopleFrom, peopleTo, query);

        return query.getSingleResult();
    }

    private <T> void setParamsToQuery(String name, Long countryId, Long cityId, Long typeId, LocalDate startDate, Integer peopleFrom, Integer peopleTo, TypedQuery<T> query) {
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", name);
        }
        if (countryId != null) {
            query.setParameter("countryId", countryId);
        }
        if (cityId != null) {
            query.setParameter("cityId", cityId);
        }
        if (typeId != null) {
            query.setParameter("typeId", typeId);
        }
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (peopleFrom != null) {
            query.setParameter("peopleFrom", peopleFrom);
        }
        if (peopleTo != null) {
            query.setParameter("peopleTo", peopleTo);
        }
    }

    private void buildJpqlQueryWithParams(String name, Long countryId, Long cityId, Long typeId,
                                          LocalDate startDate, Integer peopleFrom, Integer peopleTo, StringBuilder baseQuery) {
        if (name != null && !name.isEmpty()) {
            baseQuery.append(" AND t.name ilike concat(:name, '%')");
        }
        if (countryId != null) {
            baseQuery.append(" AND r.country.id = :countryId");
        }
        if (cityId != null) {
            baseQuery.append(" AND r.city.id = :cityId");
        }
        if (typeId != null) {
            baseQuery.append(" AND tp.id = :typeId");
        }
        if (startDate != null) {
            baseQuery.append(" AND t.startDate >= :startDate");
        }
        if (peopleFrom != null) {
            baseQuery.append(" AND t.peopleLimit >= :peopleFrom");
        }
        if (peopleTo != null) {
            baseQuery.append(" AND t.peopleLimit <= :peopleTo");
        }
    }
}
