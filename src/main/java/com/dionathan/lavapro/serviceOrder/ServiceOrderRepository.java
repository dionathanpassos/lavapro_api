package com.dionathan.lavapro.serviceOrder;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.payment.PaymentStatus;
import com.dionathan.lavapro.report.projection.CustomerProjection;
import com.dionathan.lavapro.report.projection.ServiceOrderStatusSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    Optional<ServiceOrder> findByIdAndCompanyAndDeletedAtIsNull(Long id, Company company);
    Optional<ServiceOrder> findByIdAndCompany(Long id, Company company);
    Long countByCompanyAndStatus(Company company, ServiceOrderStatus serviceOrderStatus);
    Long countByCompanyAndStatusAndCreatedAtBetween(Company company, ServiceOrderStatus serviceOrderStatus, LocalDateTime startOfDay, LocalDateTime endOfDay);
    Long countByCompanyAndStatusNotAndCreatedAtBetween(Company company, ServiceOrderStatus serviceOrderStatus, LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query(value = "SELECT s AS serviceOrder, " +
            "(CASE WHEN EXISTS (SELECT 1 FROM Payment p WHERE p.serviceOrder = s AND p.paymentStatus = :paymentStatus) THEN true ELSE false END) AS isPaid " +
            "FROM ServiceOrder s " +
            "JOIN s.vehicle v " +
            "JOIN v.customer c " +
            "WHERE s.company = :company " +
            "AND (:startDate IS NULL OR s.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR s.createdAt <= :endDate) " +
            "AND (:status IS NULL OR s.status = :status) " +
            "AND (:search IS NULL OR :search = '' " +
            "     OR LOWER(c.name) LIKE CONCAT('%', LOWER(:search), '%') " +
            "     OR LOWER(v.plate) LIKE CONCAT('%', LOWER(:search), '%'))")
    Page<ServiceOrderProjection> findAllByCompanyAndFilters(
            @Param("company") Company company,
            @Param("status") ServiceOrderStatus status,
            @Param("customer") String customer,
            @Param("plate") String plate,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("search") String search,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            Pageable pageable
    );

    @Query("SELECT " +
            "COALESCE(COUNT(CASE WHEN :company IS NULL OR s.company = :company THEN 1 END), 0) " +
            "FROM ServiceOrder s " +
            "WHERE s.company = :company " +
            "AND (:startDate IS NULL OR s.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR s.createdAt <= :endDate) "
            )
    Long countByCompanyAndPeriod(
            @Param("company") Company company,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT o.status AS serviceOrderStatus, " +
            "COUNT(o) as total " +
            "FROM ServiceOrder o " +
            "WHERE o.company = :company " +
            "AND (:startDate IS NULL OR o.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR o.createdAt <= :endDate) " +
            "GROUP BY o.status " +
            "ORDER BY COUNT(o) DESC")
    List<ServiceOrderStatusSummaryProjection> findTotalOsGroupedByStatus(
            @Param("company") Company company,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT COUNT(s) FROM ServiceOrder s WHERE " +
            "s.company = :company AND " +
            "s.status = :status " +
            "AND (:startDate IS NULL OR s.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR s.createdAt <= :endDate)")
    Long countOrders(
            @Param("company") Company company,
            @Param("status") ServiceOrderStatus status,
            @Param("startDate") LocalDateTime startOfDay,
            @Param("endDate") LocalDateTime endOfDay
    );

    @Query("SELECT " +
            "v.customer.id AS customerId, " +
            "v.customer.name AS customerName, " +
            "COALESCE(SUM(p.amount), 0) AS amountPayment, " +
            "COUNT(so) AS totalOrders " +
            "FROM ServiceOrder so " +
            "JOIN so.vehicle v " +
            "JOIN Payment p ON p.serviceOrder = so " +
            "WHERE so.company = :company " +
            "AND so.status <> :status " +
            "AND p.paymentStatus = :paymentStatus " +
            "GROUP BY v.customer.id " +
            "ORDER BY COUNT(so) DESC ")
    List<CustomerProjection> countRepeat(
            @Param("company") Company company,
            @Param("status") ServiceOrderStatus status,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT " +
            "v.customer.id AS customerId, " +
            "v.customer.name AS customerName, " +
            "v.customer.phone AS customerPhone, " +
            "COALESCE(SUM(p.amount), 0) AS amountPayment, " +
            "COUNT(so) AS totalOrders " +
            "FROM ServiceOrder so " +
            "JOIN so.vehicle v " +
            "JOIN Payment p ON p.serviceOrder = so " +
            "WHERE so.company = :company " +
                "AND so.status <> :status " +
                "AND p.paymentStatus = :paymentStatus " +
                "AND (:startDate IS NULL OR so.createdAt >= :startDate) " +
                "AND (:endDate IS NULL OR so.createdAt <= :endDate) " +
                "AND (:search IS NULL OR :search = '' OR LOWER(v.customer.name) LIKE CONCAT('%', LOWER(:search), '%')) " +
            "GROUP BY v.customer.id " )
    Page<CustomerProjection> findAllByCustomerMetrics(
            @Param("company") Company company,
            @Param("status") ServiceOrderStatus status,
            @Param("paymentStatus") PaymentStatus paymentStatus,
            @Param("search") String search,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );





}
