package com.dionathan.lavapro.cashFlow;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.payment.Payment;
import com.dionathan.lavapro.serviceOrder.ServiceOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cash_flows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CashFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CashFlowType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CashFlowCategory category;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "service_order_id", nullable = false)
    private ServiceOrder serviceOrder;

    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    public static CashFlow registerIncome(Payment payment) {
        CashFlow cashFlow = new CashFlow();

        cashFlow.type = CashFlowType.INCOME;
        cashFlow.category = CashFlowCategory.PAYMENT;
        cashFlow.amount = payment.getAmount();
        cashFlow.company = payment.getCompany();
        cashFlow.serviceOrder = payment.getServiceOrder();
        cashFlow.payment = payment;

        return cashFlow;

    }

    public static CashFlow registerRefund(Payment payment) {
        CashFlow cashFlow = new CashFlow();

        cashFlow.type = CashFlowType.EXPENSE;
        cashFlow.category = CashFlowCategory.REFUND;
        cashFlow.amount = payment.getAmount();
        cashFlow.company = payment.getCompany();
        cashFlow.serviceOrder = payment.getServiceOrder();
        cashFlow.payment = payment;

        return cashFlow;
    }
}
