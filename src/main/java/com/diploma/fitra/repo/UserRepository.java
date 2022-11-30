package com.diploma.fitra.repo;

import com.diploma.fitra.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
