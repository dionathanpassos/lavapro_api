package com.dionathan.lavapro.customer;

import com.dionathan.lavapro.company.Company;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByPhoneAndCompany(String phone, Company company);

   Optional<Customer> findByIdAndCompany(Long id, Company companyid);

    Page<Customer> findAllByCompany(Company company, Pageable pageable);

    Long countByCompany(Company company);

    @Query("SELECT c FROM customer c " +
            "LEFT JOIN Vehicle v ON v.customer = c " +
            "WHERE c.company = :company " +
            "AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:phone IS NULL OR c.phone LIKE CONCAT('%', :phone, '%')) " +
            "AND (:plate IS NULL OR LOWER(v.plate) LIKE LOWER(CONCAT('%', :plate, '%'))) ")
    Page<Customer> findAllByCompanyAndFilters(
            Pageable pageable,
            Company company,
            String name,
            String phone,
            String plate

    );

    @Query("SELECT c FROM customer c " +
            "LEFT JOIN FETCH c.vehicles " +
            "WHERE c.company = :company " +
            "AND c.id = :id ")
    Optional<Customer> findByIdAndCompanyWithVehicles(Long id, Company company);
}
