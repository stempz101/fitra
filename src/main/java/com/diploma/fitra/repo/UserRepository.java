package com.diploma.fitra.repo;

import com.diploma.fitra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByConfirmToken(String confirmToken);

    boolean existsByEmail(String email);
}
