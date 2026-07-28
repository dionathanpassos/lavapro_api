package com.dionathan.lavapro.serviceOrder;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderResponseDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    Optional<ServiceOrder> findByIdAndCompanyAndDeletedAtIsNull(Long id, Company company);

    Page<ServiceOrder> findAllByCompanyAndDeletedAtIsNull(Company company, Pageable pageable);

    Page<ServiceOrder> findAllByCompanyAndStatus(Company company, ServiceOrderStatus status, Pageable pageable);

    Optional<ServiceOrder> findByIdAndCompany(Long id, Company company);

    Long countByCompanyAndStatus(Company company, ServiceOrderStatus serviceOrderStatus);

    Long countByCompanyAndStatusAndCreatedAtBetween(Company company, ServiceOrderStatus serviceOrderStatus, LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT s FROM ServiceOrder s " +
            "JOIN s.vehicle v " +
            "JOIN v.customer c " +
            "WHERE s.company = :company " +
            "AND (:status IS NULL OR s.status = :status) " +
            "AND (:customer IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :customer, '%'))) " +
            "AND (:plate IS NULL OR LOWER(v.plate) LIKE LOWER(CONCAT('%', :plate, '%'))) " +
            "AND (:startDate IS NULL OR s.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR s.createdAt <= :endDate) ")
    Page<ServiceOrder> findAllByCompanyAndFilters(
            @Param("company") Company company,
            @Param("status") ServiceOrderStatus status,
            @Param("customer") String customer,
            @Param("plate") String plate,
            @Param("startDate")LocalDateTime startDate,
            @Param("endDate")LocalDateTime endDate,
            Pageable pageable
    );
}
