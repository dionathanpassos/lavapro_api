package com.dionathan.lavapro.user;

import com.dionathan.lavapro.company.Company;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<UserDetails> findByEmail(String email);

    @Query("SELECT u FROM User u " +
            "WHERE u.company = :company " +
            "AND (:name IS NULL OR LOWER(u.name) LIKE CONCAT('%', LOWER(:name), '%')) ")
    Page<User> findAllByCompanyAndFilters(
            Company company,
            String name,
            Pageable pageable
    );

    Optional<User> findByIdAndCompany(Long id, Company company);

    boolean existsByEmailAndIdNot(String email, Long id);
}
