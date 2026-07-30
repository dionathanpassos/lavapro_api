package com.dionathan.lavapro.vehicle;

import com.dionathan.lavapro.company.Company;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByPlateAndCompany(String plate, Company company);

    Optional<Vehicle> findByIdAndCompanyAndDeletedAtIsNull(Long id, Company company);

    Page<Vehicle> findAllByCompanyAndDeletedAtIsNull(Company company, Pageable pageable);

    boolean existsByPlateAndCompanyAndIdNot(String plate, Company company, Long id);

    Optional<Vehicle> findByPlateAndCompanyAndDeletedAtIsNull(String plate, Company company);

    @Query("SELECT v FROM Vehicle v " +
            "JOIN v.customer c " +
            "WHERE v.company = :company " +
            "AND (:plate IS NULL OR LOWER(v.plate) LIKE CONCAT('%', LOWER(:plate), '%')) " +
            "AND (:model IS NULL OR LOWER(v.model) LIKE CONCAT('%', LOWER(:model), '%')) " +
            "AND (:brand IS NULL OR LOWER(v.brand) LIKE CONCAT('%', LOWER(:brand), '%')) " +
            "AND (:customer IS NULL OR LOWER(c.name) LIKE CONCAT('%', LOWER(:customer), '%')) ")
    Page<Vehicle> findAllByCompanyAndFilters(
            Company company,
            String plate,
            String customer,
            String model,
            String brand,
            Pageable pageable
    );
}
