package com.dionathan.lavapro.payment;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.dashboard.dto.FinancialDashboardGroupByDateDTO;
import com.dionathan.lavapro.payment.dto.PaymentIndicatorsDTO;
import com.dionathan.lavapro.serviceOrder.ServiceOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByServiceOrderAndPaymentStatus(ServiceOrder serviceOrder, PaymentStatus paymentStatus);

    Optional<Payment> findByIdAndCompany(Long id, Company company);

    Page<Payment> findAllByCompany(Company company, Pageable pageable);

    List<Payment> findAllByCompanyAndServiceOrder(Company company, ServiceOrder serviceOrder);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.company = :company AND p.paymentStatus = :status AND p.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumRevenueByPeriod(
            @Param("company") Company company,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") PaymentStatus status
            );

    Long countByCompanyAndPaymentStatusAndCreatedAtBetween(
            Company company,
            PaymentStatus paymentStatus,
            LocalDateTime startDateOfDay,
            LocalDateTime endDateOfDay);

    @Query("SELECT " +
            "p.id AS id, " +
            "p.amount AS amount, " +
            "p.paymentMethod AS paymentMethod, " +
            "p.paymentStatus AS paymentStatus, " +
            "p.paidAt AS paidAt, " +
            "so.id AS serviceOrderId, " +
            "c.name AS customerName, " +
            "v.plate AS vehiclePlate, " +
            "v.brand AS vehicleBrand, " +
            "v.model AS vehicleModel " +
            "FROM Payment p " +
            "JOIN p.serviceOrder so " +
            "JOIN so.vehicle v " +
            "JOIN v.customer c " +
            "WHERE p.company = :company " +
            "AND (:paymentMethod IS NULL OR p.paymentMethod = :paymentMethod) " +
            "AND (:paymentStatus IS NULL OR p.paymentStatus = :paymentStatus) " +
            "AND (:startDate IS NULL OR p.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR p.createdAt <= :endDate) " +
            "AND (:search IS NULL OR :search = ''  OR ( " +
            "     UPPER(c.name) LIKE UPPER(CONCAT('%', :search, '%')) " +
            "     OR UPPER(v.model) LIKE UPPER(CONCAT('%', :search, '%')) " +
            "     OR UPPER(v.brand) LIKE UPPER(CONCAT('%', :search, '%')) " +
            "     OR UPPER(v.plate) LIKE UPPER(CONCAT('%', :search, '%')))) ")
    Page<PaymentListProjection> findAllByCompanyAndFilters(
            Company company,
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus,
            String search,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    @Query("""
    SELECT new com.dionathan.lavapro.dashboard.dto.FinancialDashboardGroupByDateDTO(
        CAST(p.createdAt AS LocalDate),
        SUM(p.amount)
    )
    FROM Payment p
    WHERE p.company = :company
      AND p.paymentStatus = :status
      AND p.createdAt BETWEEN :startDate AND :endDate
    GROUP BY DATE(p.createdAt)
    ORDER BY DATE(p.createdAt)
    """)
    List<FinancialDashboardGroupByDateDTO> findAllByCompanyAndStatusAndCreatedAtBetweenGroupByCreatedAt(
            @Param("company") Company company,
            @Param("status") PaymentStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    Long countByCompanyAndPaymentStatus(Company company, PaymentStatus paymentStatus);

    @Query("SELECT new com.dionathan.lavapro.payment.dto.PaymentIndicatorsDTO(" +
            "  COUNT(p), " +
            "  COALESCE(SUM(CASE WHEN p.paymentStatus = 'PAID' THEN 1 ELSE 0 END), 0L), " +
            "  COALESCE(SUM(CASE WHEN p.paymentStatus = 'CANCELED' THEN 1 ELSE 0 END), 0L)" +
            ") " +
            "FROM Payment p " +
            "JOIN p.serviceOrder so " +
            "JOIN so.vehicle v " +
            "JOIN v.customer c " +
            "WHERE p.company = :company " +
            "AND (:paymentMethod IS NULL OR p.paymentMethod = :paymentMethod) " +
            "AND (:paymentStatus IS NULL OR p.paymentStatus = :paymentStatus) " +
            "AND (:startDate IS NULL OR p.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR p.createdAt <= :endDate) " +
            "AND (:search IS NULL OR :search = ''  OR ( " +
            "     UPPER(c.name) LIKE UPPER(CONCAT('%', :search, '%')) " +
            "     OR UPPER(v.model) LIKE UPPER(CONCAT('%', :search, '%')) " +
            "     OR UPPER(v.brand) LIKE UPPER(CONCAT('%', :search, '%')) " +
            "     OR UPPER(v.plate) LIKE UPPER(CONCAT('%', :search, '%')))) ")
    PaymentIndicatorsDTO getPaymentIndicators(
            @Param("company") Company company,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            @Param("paymentMethod") PaymentMethod paymentMethod,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("search") String search
    );
}
