package com.dionathan.lavapro.cashFlow;

import com.dionathan.lavapro.cashFlow.dto.CashFlowResponseDTO;
import com.dionathan.lavapro.common.exception.BusinessException;
import com.dionathan.lavapro.common.exception.ResourceNotFoundException;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.payment.Payment;
import com.dionathan.lavapro.payment.PaymentRepository;
import com.dionathan.lavapro.payment.PaymentStatus;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public List<CashFlowResponseDTO> findAll() {
        Company company = getCurrentCompany();

        List<CashFlow> cashFlows = cashFlowRepository.findAllByCompany(company);

        return cashFlowMapper.fromEntity(cashFlows);
    }
    @Transactional(readOnly = true)
    public CashFlowResponseDTO findById(Long id) {
        Company company = getCurrentCompany();

        CashFlow cashFlow = cashFlowRepository.findByIdAndCompany(id, company)
                .orElseThrow(() -> new ResourceNotFoundException("Registro não encontrado."));

        return cashFlowMapper.fromEntity(cashFlow);
    }

    private Company getCurrentCompany() {
        return authenticatedUserService.getAuthenticatedUser().getCompany();
    }



}
