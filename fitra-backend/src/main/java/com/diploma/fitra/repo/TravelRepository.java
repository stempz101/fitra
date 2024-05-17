package com.diploma.fitra.repo;

import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {

    @Query("select t from _travels t " +
            "where t in (select p.travel from _participants p where p.user = ?1) " +
            "order by t.startDate")
    List<Travel> findAllForUserOrderByStartDate(User user, Pageable pageable);

    List<Travel> findAllByCreatorIdOrderByStartDate(Long creatorId, Pageable pageable);

    @Query("select count(t) from _travels t " +
            "where t in (select p.travel from _participants p where p.user = ?1)")
    long countAllForUser(User user);

    long countAllByCreatorId(Long creatorId);
}
