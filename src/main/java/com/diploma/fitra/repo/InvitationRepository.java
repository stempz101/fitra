package com.diploma.fitra.repo;

import com.diploma.fitra.model.Invitation;
import com.diploma.fitra.model.key.InvitationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, InvitationKey> {
}
