package com.dionathan.lavapro.cashFlow;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
                    "AND c.createdAt BETWEEN :startDate AND :endDate"
    )
    List<CashFlow> findAllByCompanyAndFilters(
            @Param("company") Company company,
            @Param("category") CashFlowCategory category,
            @Param("type") CashFlowType type,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate")LocalDateTime endDate
            );
}
