package com.dionathan.lavapro.cashFlow;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.payment.Payment;
import com.dionathan.lavapro.payment.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CashFlowRepository extends JpaRepository<CashFlow, Long> {
    List<CashFlow> findAllByCompany(Company company);

    boolean existsByPaymentAndCategory(Payment payment, CashFlowCategory cashFlowCategory);

    Optional<CashFlow> findByIdAndCompany(Long id, Company company);


    @Query("SELECT c FROM CashFlow c WHERE c.company = :company " +
            "AND (:category IS NULL OR c.category = :category) " +
            "AND (:type IS NULL OR c.type = :type) " +
            "AND (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) " +
            "AND (:paymentMethod IS NULL OR c.payment.paymentMethod = :paymentMethod) " +
            "AND (:search IS NULL OR LOWER(c.serviceOrder.vehicle.customer.name) LIKE LOWER(CONCAT('%', :search, '%'))) "
    )
    Page<CashFlow> findAllByCompanyAndFilters(
            @Param("company") Company company,
            @Param("category") CashFlowCategory category,
            @Param("search") String search,
            @Param("type") CashFlowType type,
            @Param("paymentMethod") PaymentMethod paymentMethod,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate")LocalDateTime endDate,
            Pageable pageable
            );

    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM CashFlow c " +
            "WHERE c.company = :company " +
            "AND (:type IS NULL OR c.type = :type) " +
            "AND (:category IS NULL OR c.category = :category) " +
            "AND (:startDate IS NULL OR c.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR c.createdAt <= :endDate) ")
    BigDecimal sumCashFlowByPeriodAndTypeAndCategory(
            @Param("company") Company company,
            @Param("type")CashFlowType type,
            @Param("category")CashFlowCategory category,
            @Param("startDate")LocalDateTime startDate,
            @Param("endDate")LocalDateTime endDate
    );

    @Query("""
    SELECT
        COALESCE(SUM(CASE WHEN c.type = :incomeType THEN c.amount ELSE 0 END ), 0) AS income,
        COALESCE(SUM(CASE WHEN c.type = :expenseType THEN c.amount ELSE 0 END ),  0 ) AS expense
    FROM CashFlow c
    WHERE c.company = :company
    AND (:type IS NULL OR c.type = :type)
    AND (:category IS NULL OR c.category = :category)
    AND (:paymentMethod IS NULL OR c.payment.paymentMethod = :paymentMethod)
    AND (:startDate IS NULL OR c.createdAt >= :startDate)
    AND (:endDate IS NULL OR c.createdAt <= :endDate)
    AND (:search IS NULL OR LOWER(c.serviceOrder.vehicle.customer.name) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    CashFlowIndicatorsProjection getIndicators(
            @Param("company") Company company,
            @Param("type") CashFlowType type,
            @Param("incomeType") CashFlowType incomeType,
            @Param("expenseType") CashFlowType expenseType,
            @Param("category") CashFlowCategory category,
            @Param("search") String search,
            @Param("paymentMethod") PaymentMethod paymentMethod,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT COALESCE(
        SUM(
            CASE
                WHEN c.type = :incomeType THEN c.amount
                WHEN c.type = :expenseType THEN -c.amount
                ELSE 0
            END
        ),
        0
    )
    FROM CashFlow c
    WHERE c.company = :company
    """)
    BigDecimal getBalance(
            @Param("company") Company company,
            @Param("incomeType") CashFlowType incomeType,
            @Param("expenseType") CashFlowType expenseType
    );
}
