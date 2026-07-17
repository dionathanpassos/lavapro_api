package com.dionathan.lavapro.payment;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.serviceOrder.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByServiceOrderAndPaymentStatus(ServiceOrder serviceOrder, PaymentStatus paymentStatus);

    Optional<Payment> findByIdAndCompany(Long id, Company company);

    List<Payment> findAllByCompany(Company company);
}
