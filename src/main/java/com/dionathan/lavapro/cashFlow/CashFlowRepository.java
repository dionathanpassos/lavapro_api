package com.dionathan.lavapro.cashFlow;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CashFlowRepository extends JpaRepository<CashFlow, Long> {
    List<CashFlow> findAllByCompany(Company company);

    boolean existsByPaymentAndCategory(Payment payment, CashFlowCategory cashFlowCategory);

    Optional<CashFlow> findByIdAndCompany(Long id, Company company);
}
