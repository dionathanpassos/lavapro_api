package com.dionathan.lavapro.serviceOrder;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderResponseDTO;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    Optional<ServiceOrder> findByIdAndCompanyAndDeletedAtIsNull(Long id, Company company);

    Page<ServiceOrder> findAllByCompanyAndDeletedAtIsNull(Company company, Pageable pageable);

    Page<ServiceOrder> findAllByCompanyAndStatus(Company company, ServiceOrderStatus status, Pageable pageable);

    Optional<ServiceOrder> findByIdAndCompany(Long id, Company company);

    Long countByCompanyAndStatus(Company company, ServiceOrderStatus serviceOrderStatus);

    Long countByCompanyAndStatusAndCreatedAtBetween(Company company, ServiceOrderStatus serviceOrderStatus, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
