package com.diploma.fitra.repo;

import com.diploma.fitra.model.Invitation;
import com.diploma.fitra.model.Travel;
import com.diploma.fitra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    List<Invitation> findAllByUser(User user);

    List<Invitation> findAllByTravel_Creator(User creator);

    List<Invitation> findAllByTravelAndUser(Travel travel, User user);

//    Optional<Invitation> findByTravelAndUser(Travel travel, User user);
}
