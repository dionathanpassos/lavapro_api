package com.dionathan.lavapro.serviceOrder;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    Optional<ServiceOrder> findByIdAndCompanyAndDeletedAtIsNull(Long id, Company company);

    Page<ServiceOrder> findAllByCompanyAndDeletedAtIsNull(Company company, Pageable pageable);

    Page<ServiceOrder> findAllByCompanyAndStatus(Company company, ServiceOrderStatus status, Pageable pageable);
}
