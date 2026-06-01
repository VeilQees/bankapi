package com.zaitsev.bankapi.repository;

import com.zaitsev.bankapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromUserOrToUser(String from, String to);

    @Query("""
        SELECT t
        FROM Transaction t
        WHERE
        (t.fromUser = :username
        OR t.toUser = :username)
        AND
        t.createdAt BETWEEN :start AND :end
       """)
    List<Transaction> findAllByUserAndDateBetween(
            @Param("username") String username,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}