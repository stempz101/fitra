package com.diploma.fitra.repo;

import com.diploma.fitra.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from _users u where upper(u.fullName) like upper(concat(?1, '%'))")
    List<User> findAllByFullNameContainingIgnoreCase(String search, Pageable pageable);

    @Query("select u from _users u where not exists (" +
            "select p from _participants p where p.travel.id = ?1 and p.user.id = u.id" +
            ") and upper(u.fullName) like upper(concat('%', ?2, '%'))")
    List<User> findAllNotInTravel(Long travelId, String search, Pageable pageable);

    Optional<User> findByEmail(String email);

    Optional<User> findByConfirmToken(String confirmToken);

    boolean existsByEmail(String email);

    @Query("select count(distinct u.id) from _users u " +
            "where u.fullName ilike concat(?1, '%') and u.country.id = ?2 and u.city.id = ?3")
    long countByQueryParams(String name, Long countryId, Long cityId);
}
