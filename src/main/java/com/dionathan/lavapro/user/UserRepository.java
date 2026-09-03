package com.dionathan.lavapro.user;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.user.dto.UserIndicatorsDTO;
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
            "AND (:name IS NULL OR LOWER(u.name) LIKE CONCAT('%', LOWER(:name), '%')) " +
            "AND (:active IS NULL OR u.active = :active) ")
    Page<User> findAllByCompanyAndFilters(
            Company company,
            String name,
            Boolean active,
            Pageable pageable
    );

    Optional<User> findByIdAndCompany(Long id, Company company);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("SELECT " +
            "COALESCE(COUNT(CASE WHEN :active IS NULL OR u.active = :active THEN 1 END), 0) AS totalUsers, " +
            "COALESCE(COUNT(CASE WHEN u.active = true THEN 1 END), 0) AS totalActive, " +
            "COALESCE(COUNT(CASE WHEN u.active = false THEN 1 END), 0) AS totalInactive " +
            "FROM User u " +
            "WHERE u.company = :company " +
            "AND (:name IS NULL OR LOWER(u.name) LIKE CONCAT('%', LOWER(:name), '%')) " +
            "AND (:active IS NULL OR u.active = :active) ")
    UserIndicatorsDTO getIndicators(Company company, String name, Boolean active);
}
