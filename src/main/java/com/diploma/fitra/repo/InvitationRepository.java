package com.diploma.fitra.repo;

import com.diploma.fitra.model.Invitation;
import com.diploma.fitra.model.User;
import com.diploma.fitra.model.key.InvitationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, InvitationKey> {

    List<Invitation> findAllByUser(User user);
}
