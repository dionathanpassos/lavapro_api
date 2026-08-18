package com.dionathan.lavapro.dashboard;

import com.dionathan.lavapro.cashFlow.CashFlowCategory;
import com.dionathan.lavapro.cashFlow.CashFlowRepository;
import com.dionathan.lavapro.cashFlow.CashFlowType;
import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.customer.CustomerRepository;
import com.dionathan.lavapro.dashboard.dto.*;
import com.dionathan.lavapro.payment.PaymentRepository;
import com.dionathan.lavapro.payment.PaymentStatus;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import com.dionathan.lavapro.serviceOrder.ServiceOrderRepository;
import com.dionathan.lavapro.serviceOrder.ServiceOrderStatus;
import com.dionathan.lavapro.serviceOrderitem.ServiceOrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AuthenticatedUserService authenticatedUserService;
    private final ServiceOrderRepository serviceOrderRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private final DateManager dateManager;
    private final CashFlowRepository cashFlowRepository;
    private final ServiceOrderItemRepository serviceOrderItemRepository;

    @Transactional(readOnly = true)
    public DashboardResponseDTO getDashboard() {

        ServiceOrderDashboardDTO serviceOrders = getServiceOrderIndicators();
        CustomerDashboardDTO customers = getCustomerIndicators();
        FinancialDashboardDTO financial = getFinancialIndicators();
        CashFlowDashboardDTO cashFlow = getCashFlowIndicators();
        List<BestSellingServiceDTO> services = getServiceIndicators();

        return new DashboardResponseDTO(
                serviceOrders,
                customers,
                financial,
                cashFlow,
                services

        );
    }

    public Company getCurrentCompany() {
        return authenticatedUserService.getAuthenticatedUser().getCompany();
    }

    private ServiceOrderDashboardDTO getServiceOrderIndicators() {
        Company company = getCurrentCompany();

        DateRangeDTO currentDay = dateManager.getCustomDayRange(LocalDate.now());
        DateRangeDTO currentMonth = dateManager.getCurrentMonthRange();

        Long waiting = serviceOrderRepository.countByCompanyAndStatusAndCreatedAtBetween(
                company, ServiceOrderStatus.WAITING, currentDay.startDate(), currentDay.endDate());

        Long inProgress = serviceOrderRepository.countByCompanyAndStatusAndCreatedAtBetween(
                company, ServiceOrderStatus.IN_PROGRESS, currentDay.startDate(), currentDay.endDate());

        Long ready = serviceOrderRepository.countByCompanyAndStatusAndCreatedAtBetween(
                company, ServiceOrderStatus.READY, currentDay.startDate(), currentDay.endDate());

        Long delivered = serviceOrderRepository.countByCompanyAndStatusAndCreatedAtBetween(
                company, ServiceOrderStatus.DELIVERED, currentDay.startDate(), currentDay.endDate());

        Long canceled = serviceOrderRepository.countByCompanyAndStatusAndCreatedAtBetween(
                company, ServiceOrderStatus.CANCELLED, currentDay.startDate(), currentDay.endDate());

        Long totalMonth = serviceOrderRepository.countByCompanyAndStatusNotAndCreatedAtBetween(
                company, ServiceOrderStatus.CANCELLED, currentMonth.startDate(), currentMonth.endDate());

        return new ServiceOrderDashboardDTO(
                waiting,
                inProgress,
                ready,
                delivered,
                canceled,
                totalMonth
        );
    }

    private CustomerDashboardDTO getCustomerIndicators() {
        Company company = getCurrentCompany();

        Long customers = customerRepository.countByCompany(company);

        return new CustomerDashboardDTO(
                customers
        );

    }

    private FinancialDashboardDTO getFinancialIndicators() {
        Company company = getCurrentCompany();

        DateRangeDTO currentMonth = dateManager.getCurrentMonthRange();
        DateRangeDTO currentDay = dateManager.getCustomDayRange(LocalDate.now());

        BigDecimal monthRevenue =
                paymentRepository.sumRevenueByPeriod(company, currentMonth.startDate(), currentMonth.endDate(), PaymentStatus.PAID);

        BigDecimal todayRevenue =
                paymentRepository.sumRevenueByPeriod(company, currentDay.startDate(), currentDay.endDate(), PaymentStatus.PAID);

        Long countRefunded =
                paymentRepository.countByCompanyAndPaymentStatusAndCreatedAtBetween(company, PaymentStatus.CANCELED, currentDay.startDate(), currentDay.endDate());

        Long countPaid =
                paymentRepository.countByCompanyAndPaymentStatusAndCreatedAtBetween(company, PaymentStatus.PAID, currentMonth.startDate(), currentMonth.endDate());

        BigDecimal averageTicket = calculateAverageTicket(countPaid, monthRevenue);

        return new FinancialDashboardDTO(
                monthRevenue,
                todayRevenue,
                averageTicket,
                countRefunded,
                countPaid
        );
    }

    private CashFlowDashboardDTO getCashFlowIndicators() {
        Company company = getCurrentCompany();

        DateRangeDTO currentMonth = dateManager.getCurrentMonthRange();

        BigDecimal monthIncome = cashFlowRepository.sumCashFlowByPeriodAndTypeAndCategory(
                company,
                CashFlowType.INCOME,
                CashFlowCategory.PAYMENT,
                currentMonth.startDate(),
                currentMonth.endDate()
        );

        BigDecimal monthExpense = cashFlowRepository.sumCashFlowByPeriodAndTypeAndCategory(
                company,
                CashFlowType.EXPENSE,
                CashFlowCategory.REFUND,
                currentMonth.startDate(),
                currentMonth.endDate()
        );

        BigDecimal balance = monthIncome.subtract(monthExpense);

        return new CashFlowDashboardDTO(
                monthIncome,
                monthExpense,
                balance
        );
    }

    private List<BestSellingServiceDTO> getServiceIndicators() {
        Company company = getCurrentCompany();

        DateRangeDTO currentMonth = dateManager.getCurrentMonthRange();

        List<BestSellingServiceDTO> services = serviceOrderItemRepository.findBestSellingService(company, currentMonth.startDate(), currentMonth.endDate(), PaymentStatus.PAID);

        return services;

//        services.stream()
//                .findFirst()
//                .orElse(new BestSellingServiceDTO("Nenhum serviço", 0L));

    }

    private BigDecimal calculateAverageTicket(Long countPayment, BigDecimal todayRevenue) {
        if(countPayment == 0 || todayRevenue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return todayRevenue.divide(BigDecimal.valueOf(countPayment), 2, RoundingMode.HALF_UP);
    }
}
