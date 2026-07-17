package com.dionathan.lavapro.payment;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.payment.dto.PaymentRequestDTO;
import com.dionathan.lavapro.payment.dto.PaymentResponseDTO;
import com.dionathan.lavapro.serviceOrder.ServiceOrder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PaymentMapper {

    public Payment toEntity(Company company, ServiceOrder serviceOrder, PaymentRequestDTO requestDTO) {
        Payment payment = new Payment();

        payment.setAmount(serviceOrder.getTotalAmount());
        payment.setPaymentMethod(requestDTO.paymentMethod());
        payment.setPaidAt(LocalDateTime.now());
        payment.setCompany(company);
        payment.setServiceOrder(serviceOrder);

        return payment;

    }

    public PaymentResponseDTO fromEntity(Payment payment) {
        return new PaymentResponseDTO(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getPaidAt(),
                payment.getCreatedAt(),
                payment.getUpdatedAt(),
                payment.getCanceledAt(),
                payment.getServiceOrder().getId()
        );
    }

    public List<PaymentResponseDTO> fromEntity(List<Payment> payments) {
        return payments.stream().map(this::fromEntity).toList();
    }
}
