package com.diploma.fitra.repo;

import com.diploma.fitra.model.EmailUpdate;
import com.diploma.fitra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailUpdateRepository extends JpaRepository<EmailUpdate, Long> {

    Optional<EmailUpdate> findByConfirmToken(String confirmToken);

    Optional<EmailUpdate> findByUser(User user);
}
