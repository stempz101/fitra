package com.diploma.fitra.repo.custom.impl;

import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import com.diploma.fitra.repo.custom.UserCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAllByQueryParams(String name, Long countryId, Long cityId, Pageable pageable) {
        StringBuilder baseQuery = new StringBuilder()
                .append("SELECT u FROM _users u ")
                .append("WHERE 1=1");
        buildJpqlQueryWithParams(name, countryId, cityId, baseQuery);
        baseQuery.append(" ORDER BY u.fullName");

        TypedQuery<User> query = entityManager.createQuery(baseQuery.toString(), User.class);
        setParamsToQuery(name, countryId, cityId, query);
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
    public long countByQueryParams(String name, Long countryId, Long cityId) {
        StringBuilder baseQuery = new StringBuilder()
                .append("SELECT COUNT(DISTINCT u.id) FROM _users u ")
                .append("WHERE 1=1");
        buildJpqlQueryWithParams(name, countryId, cityId, baseQuery);

        TypedQuery<Long> query = entityManager.createQuery(baseQuery.toString(), Long.class);
        setParamsToQuery(name, countryId, cityId, query);

        return query.getSingleResult();
    }

    private void buildJpqlQueryWithParams(String name, Long countryId, Long cityId, StringBuilder baseQuery) {
        if (name != null && !name.isEmpty()) {
            baseQuery.append(" AND u.fullName ilike concat(:name, '%')");
        }
        if (countryId != null) {
            baseQuery.append(" AND u.country.id = :countryId");
        }
        if (cityId != null) {
            baseQuery.append(" AND u.city.id = :cityId");
        }
    }

    private <T> void setParamsToQuery(String name, Long countryId, Long cityId, TypedQuery<T> query) {
        if (name != null && !name.isEmpty()) {
            query.setParameter("name", name);
        }
        if (countryId != null) {
            query.setParameter("countryId", countryId);
        }
        if (cityId != null) {
            query.setParameter("cityId", cityId);
        }
    }
}
