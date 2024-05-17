package com.diploma.fitra.repo;

import com.diploma.fitra.model.UsedPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsedPasswordRepository extends JpaRepository<UsedPassword, Long> {

    List<UsedPassword> findAllByUserId(Long userId);
}
