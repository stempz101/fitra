package com.diploma.fitra.repo;

import com.diploma.fitra.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {

    boolean existsByNameEn(String nameEn);

    boolean existsByNameUa(String nameUa);
}
