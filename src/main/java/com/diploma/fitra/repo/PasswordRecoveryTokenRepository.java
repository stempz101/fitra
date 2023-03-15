package com.diploma.fitra.repo;

import com.diploma.fitra.model.PasswordRecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, Long> {

    Optional<PasswordRecoveryToken> findByUserEmail(String email);

    Optional<PasswordRecoveryToken> findByToken(String token);
}
