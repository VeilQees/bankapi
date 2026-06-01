package com.zaitsev.bankapi.repository;

import com.zaitsev.bankapi.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       SELECT a
       FROM Account a
       WHERE a.id = :id
       """)
    Optional<Account> findByIdForUpdate(
            @Param("id") Long id
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       SELECT a
       FROM Account a
       WHERE a.username = :username
       AND a.primary = true
       """)
    Optional<Account> findPrimaryAccountForUpdate(
            @Param("username") String username
    );

    List<Account> findAllByUsername(String username);

    Optional<Account> findByUsernameAndPrimaryTrue(String username);

    Optional<Account> findByNameAndUsername(String name, String username); // 🔥 НОВОЕ

    boolean existsByUsernameAndNameIgnoreCase(
            String username,
            String name
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
   SELECT a
   FROM Account a
   WHERE a.name = :name
   AND a.username = :username
   """)
    Optional<Account> findByNameAndUsernameForUpdate(
            @Param("name") String name,
            @Param("username") String username
    );
}