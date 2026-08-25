package com.dionathan.lavapro.serviceOrderitem;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.dashboard.dto.BestSellingServiceDTO;
import com.dionathan.lavapro.payment.PaymentStatus;
import com.dionathan.lavapro.serviceOrder.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ServiceOrderItemRepository extends JpaRepository<ServiceOrderItem, Long> {
    List<ServiceOrderItem> findAllByServiceOrderAndCompany(ServiceOrder serviceOrder, Company company);

    Optional<ServiceOrderItem> findByIdAndCompany(Long itemId, Company company);

    @Query("SELECT i.serviceCatalog.name, SUM(i.quantity) " +
            "FROM ServiceOrderItem i " +
            "JOIN i.serviceOrder os " +
            "JOIN Payment p ON p.serviceOrder = os " +
            "WHERE " +
            "i.company = :company " +
            "AND i.createdAt BETWEEN :startDate AND :endDate " +
            "AND p.paymentStatus = :status " +
            "GROUP BY " +
            "i.serviceCatalog.id, i.serviceCatalog.name " +
            "ORDER BY SUM(i.quantity) DESC ")
    List<BestSellingServiceDTO> findBestSellingService(
            @Param("company") Company company,
            @Param("startDate")LocalDateTime startDate,
            @Param("endDate")LocalDateTime endDate,
            @Param("status")PaymentStatus status
            );
}
