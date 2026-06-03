package com.dionathan.lavapro.ServiceCatalog;

import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogResponseDTO;
import com.dionathan.lavapro.company.Company;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceCatalogRepository extends JpaRepository<ServiceCatalog, Long> {
    Optional<ServiceCatalog> findByIdAndCompany(Long id, Company company);

    Page<ServiceCatalog> findAllByCompany(Company company, Pageable pageable);

    boolean existsByCompanyAndNameIgnoreCase(Company company, String name);
}
