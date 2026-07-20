package com.dionathan.lavapro.cashFlow;

import com.dionathan.lavapro.cashFlow.dto.CashFlowResponseDTO;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.payment.Payment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CashFlowMapper {

    public CashFlow toEntity(Payment payment, Company company, CashFlowType type, CashFlowCategory category) {
        CashFlow cashFlow = new CashFlow();

        cashFlow.setType(type);
        cashFlow.setCategory(category);
        cashFlow.setAmount(payment.getAmount());
        cashFlow.setCompany(company);
        cashFlow.setServiceOrder(payment.getServiceOrder());
        cashFlow.setPayment(payment);

        return cashFlow;
    }

    public CashFlowResponseDTO fromEntity(CashFlow cashFlow) {
        return new CashFlowResponseDTO(
                cashFlow.getId(),
                cashFlow.getType(),
                cashFlow.getCategory(),
                cashFlow.getAmount(),
                cashFlow.getCreatedAt(),
                cashFlow.getUpdatedAt(),
                cashFlow.getCompany().getId(),
                cashFlow.getServiceOrder().getId(),
                cashFlow.getPayment().getId()
        );
    }

    public List<CashFlowResponseDTO> fromEntity(List<CashFlow> cashFlows) {
        return cashFlows.stream().map(this::fromEntity).toList();
    }
}
