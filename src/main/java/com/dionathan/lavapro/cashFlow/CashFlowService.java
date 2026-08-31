package com.dionathan.lavapro.cashFlow;

import com.dionathan.lavapro.cashFlow.dto.CashFlowIndicatorsDTO;
import com.dionathan.lavapro.cashFlow.dto.CashFlowResponseDTO;
import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.common.exception.ResourceNotFoundException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.payment.Payment;
import com.dionathan.lavapro.payment.PaymentMethod;
import com.dionathan.lavapro.payment.PaymentRepository;
import com.dionathan.lavapro.payment.PaymentStatus;
import com.dionathan.lavapro.payment.dto.PaymentIndicatorsDTO;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.BridgeAware;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CashFlowService {

    private final CashFlowRepository cashFlowRepository;
    private final CashFlowMapper cashFlowMapper;
    private final AuthenticatedUserService authenticatedUserService;

    @Transactional
    public void registerIncome(Payment payment) {
        Company company = getCurrentCompany();

        payment.validateIsPaid();

        if (cashFlowRepository.existsByPaymentAndCategory(payment, CashFlowCategory.PAYMENT)) {
            throw new BusinessException("Já existe uma movimentação para este pagamento.");
        }
        cashFlowRepository.save(CashFlow.registerIncome(payment));

    }

    @Transactional
    public void registerRefund(Payment payment) {
        Company company = getCurrentCompany();
        if (cashFlowRepository.existsByPaymentAndCategory(payment, CashFlowCategory.REFUND)) {
            throw new BusinessException("O estorno já foi registrado.");
        }
        cashFlowRepository.save(CashFlow.registerRefund(payment));
    }

    @Transactional(readOnly = true)
    public Page<CashFlowResponseDTO> findAll(
            CashFlowCategory category,
            String search,
            CashFlowType type,
            PaymentMethod paymentMethod,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        Company company = getCurrentCompany();

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        Page<CashFlow> cashFlows = cashFlowRepository.findAllByCompanyAndFilters(company, category, search, type, paymentMethod, startDateTime, endDateTime, pageable);

        return cashFlows.map(cashFlowMapper::fromEntity);
    }

    @Transactional(readOnly = true)
    public CashFlowResponseDTO findById(Long id) {
        Company company = getCurrentCompany();

        CashFlow cashFlow = cashFlowRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Registro não encontrado."));

        return cashFlowMapper.fromEntity(cashFlow);
    }

    @Transactional(readOnly = true)
    public CashFlowIndicatorsDTO getIndicators(
            CashFlowType type,
            CashFlowCategory category,
            String search,
            PaymentMethod paymentMethod,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Company company = getCurrentCompany();

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        CashFlowIndicatorsProjection projection = cashFlowRepository.getIndicators(
                company,
                type,
                CashFlowType.INCOME,
                CashFlowType.EXPENSE,
                category,
                search,
                paymentMethod,
                startDateTime,
                endDateTime
        );
        BigDecimal balance = projection.getIncome().subtract(projection.getExpense());
        BigDecimal balanceTotal = cashFlowRepository.getBalance(company, CashFlowType.INCOME, CashFlowType.EXPENSE);


        return new CashFlowIndicatorsDTO(
                projection.getIncome(),
                projection.getExpense(),
                balance,
                balanceTotal
        );
    }

    private Company getCurrentCompany() {
        return authenticatedUserService.getAuthenticatedUser().getCompany();
    }



}
