package com.dionathan.lavapro.payment;

import com.dionathan.lavapro.cashFlow.CashFlowService;
import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.common.exception.ResourceNotFoundException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.payment.dto.PaymentRequestDTO;
import com.dionathan.lavapro.payment.dto.PaymentResponseDTO;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import com.dionathan.lavapro.serviceOrder.ServiceOrder;
import com.dionathan.lavapro.serviceOrder.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final PaymentMapper paymentMapper;
    private final CashFlowService cashFlowService;

    @Transactional
    public PaymentResponseDTO create(PaymentRequestDTO requestDTO) {
        Company company = getCurrentCompany();

        ServiceOrder serviceOrder = serviceOrderRepository.findByIdAndCompanyAndDeletedAtIsNull(requestDTO.serviceOrderId(), company)
                .orElseThrow(() -> new ResourceNotFoundException("OS não encontrada"));

        serviceOrder.validateCanReceivePayment();

        if(paymentRepository.existsByServiceOrderAndPaymentStatus(serviceOrder, PaymentStatus.PAID)) {
            throw new BusinessException("A OS já possui um pagamento confirmado.");
        }

        Payment payment = paymentMapper.toEntity(company, serviceOrder, requestDTO);
        payment.markAsPaid();

        Payment saved = paymentRepository.save(payment);
        cashFlowService.registerIncome(saved);

        return paymentMapper.fromEntity(saved);
    }

    @Transactional
    public void cancel(Long paymentId) {
        Company company = getCurrentCompany();

        Payment payment = paymentRepository.findByIdAndCompany(paymentId, company)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado."));

        payment.cancel();
        cashFlowService.registerRefund(payment);

    }

    @Transactional(readOnly = true)
    public Page<PaymentResponseDTO> findAll(
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        Company company = getCurrentCompany();

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        Page<Payment> payments = paymentRepository.findAllByCompanyAndFilters(
                company,
                paymentMethod,
                paymentStatus,
                startDateTime,
                endDateTime,
                pageable
        );
        return payments.map(paymentMapper::fromEntity);

    }

    @Transactional(readOnly = true)
    public PaymentResponseDTO findById(Long paymentId) {
        Company company = getCurrentCompany();

        Payment payment = paymentRepository.findByIdAndCompany(paymentId, company)
                .orElseThrow(() -> new ResourceNotFoundException("PAgamento não encontrado."));

        return paymentMapper.fromEntity(payment);
    }

    private Company getCurrentCompany() {
        return authenticatedUserService.getAuthenticatedUser().getCompany();
    }

}
