package com.dionathan.lavapro.serviceOrderitem;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.serviceOrder.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceOrderItemRepository extends JpaRepository<ServiceOrderItem, Long> {
    List<ServiceOrderItem> findAllByServiceOrderAndCompany(ServiceOrder serviceOrder, Company company);

    Optional<ServiceOrderItem> findByIdAndCompany(Long itemId, Company company);
}
