package com.dionathan.lavapro.ServiceCatalog;

import com.dionathan.lavapro.ServiceCatalog.dto.ServiceCatalogResponseDTO;
import com.dionathan.lavapro.company.Company;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ServiceCatalogRepository extends JpaRepository<ServiceCatalog, Long> {
    Optional<ServiceCatalog> findByIdAndCompany(Long id, Company company);

    Page<ServiceCatalog> findAllByCompany(Company company, Pageable pageable);

    boolean existsByCompanyAndNameIgnoreCase(Company company, String name);

    Optional<ServiceCatalog> findByIdAndCompanyAndActiveIsTrue(Long id, Company company);

    boolean existsByCompanyAndNameIgnoreCaseAndIdNot(Company company, String name, Long id);

    Long countByCompany(Company company);

    Long countByCompanyAndActiveIsTrue(Company company);

    @Query("SELECT s FROM ServiceCatalog s " +
            "WHERE s.company = :company " +
            "AND (:active IS NULL OR s.active = :active) " +
            "AND (:type IS NULL OR s.type = :type) " +
            "AND (:search IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))) ")
    Page<ServiceCatalog> findAllByCompanyAndFilters(
            Company company,
            String search,
            ServiceCatalogType type,
            Boolean active,
            Pageable pageable
    );
}
