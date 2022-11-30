package com.diploma.fitra.repo;

import com.diploma.fitra.model.BlackList;
import com.diploma.fitra.model.key.BlackListKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList, BlackListKey> {
}
