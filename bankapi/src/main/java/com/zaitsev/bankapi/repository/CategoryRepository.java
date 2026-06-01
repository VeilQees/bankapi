package com.zaitsev.bankapi.repository;

import com.zaitsev.bankapi.entity.Category;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByNameIgnoreCase(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
           SELECT c
           FROM Category c
           WHERE c.id = :id
           """)
    Optional<Category> findByIdForUpdate(
            @Param("id") Long id
    );
}