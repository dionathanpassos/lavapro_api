package com.dionathan.lavapro.vehicle;

import com.dionathan.lavapro.company.Company;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByPlateAndCompany(String plate, Company company);

    Optional<Vehicle> findByIdAndCompanyAndDeletedAtIsNull(Long id, Company company);

    Page<Vehicle> findAllByCompanyAndDeletedAtIsNull(Company company, Pageable pageable);

    boolean existsByPlateAndCompanyAndIdNot(String plate, Company company, Long id);
}
