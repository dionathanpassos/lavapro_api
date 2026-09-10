package com.dionathan.lavapro.report;

import com.dionathan.lavapro.company.Company;
import com.dionathan.lavapro.customer.CustomerRepository;
import com.dionathan.lavapro.dashboard.dto.BestSellingServiceDTO;
import com.dionathan.lavapro.dashboard.dto.FinancialDashboardGroupByDateDTO;
import com.dionathan.lavapro.payment.*;
import com.dionathan.lavapro.payment.dto.PaymentListResponseDTO;
import com.dionathan.lavapro.report.dto.*;
import com.dionathan.lavapro.report.projection.CustomerProjection;
import com.dionathan.lavapro.report.projection.PaymentMethodSummaryProjection;
import com.dionathan.lavapro.report.projection.ServiceOrderStatusSummaryProjection;
import com.dionathan.lavapro.security.AuthenticatedUserService;
import com.dionathan.lavapro.serviceOrder.ServiceOrderMapper;
import com.dionathan.lavapro.serviceOrder.ServiceOrderProjection;
import com.dionathan.lavapro.serviceOrder.ServiceOrderRepository;
import com.dionathan.lavapro.serviceOrder.ServiceOrderStatus;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderProResponseDTO;
import com.dionathan.lavapro.serviceOrderitem.ServiceOrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AuthenticatedUserService authenticatedUserService;
    private final PaymentRepository paymentRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final ServiceOrderItemRepository serviceOrderItemRepository;
    private final PaymentMapper paymentMapper;
    private final ServiceOrderMapper serviceOrderMapper;
    private final CustomerRepository customerRepository;

    public OverviewReportDTO getOverviewReport(LocalDate startDate, LocalDate endDate) {
        Company company = getCurrentCompany();

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        BigDecimal revenue = paymentRepository.sumRevenueByPeriod(company, startDateTime, endDateTime, PaymentStatus.PAID);
        Long totalServiceOrder = serviceOrderRepository.countByCompanyAndPeriod(company, startDateTime, endDateTime);
        Long countPayment = paymentRepository.countByPeriod(company, startDateTime, endDateTime, PaymentStatus.PAID);
        Long paidServiceOrders = paymentRepository.countDistinctPaidServiceOrdersByPeriod(company, startDateTime, endDateTime, PaymentStatus.PAID );
        BigDecimal averageTicket = calculateAverageTicket(paidServiceOrders, revenue);
        Long distinctCustomers = paymentRepository.countDistinctCustomersByPeriod(company, startDateTime, endDateTime, PaymentStatus.PAID);

        List<FinancialDashboardGroupByDateDTO> revenueByDate = getRevenueGroupedByDate(company, PaymentStatus.PAID, startDateTime, endDateTime);

        List<PaymentMethodSummaryProjection> paymentMethods = paymentRepository.findTotalAmountGroupedByMethod(company, startDateTime, endDateTime, PaymentStatus.PAID);

        List<ServiceOrderStatusSummaryProjection> serviceOrdersStatus = serviceOrderRepository.findTotalOsGroupedByStatus(company,startDateTime, endDateTime);

        List<BestSellingServiceDTO> services = serviceOrderItemRepository.findBestSellingService(company, startDateTime, endDateTime, PaymentStatus.PAID);

        Map<PaymentMethod, BigDecimal> paymentbyMethod = getSumPaymentMethod(company, startDateTime, endDateTime, PaymentStatus.PAID);

        Map<ServiceOrderStatus, Long> serviceOrderStatusLongMap =
                serviceOrdersStatus.stream()
                        .collect(Collectors.toMap(
                                ServiceOrderStatusSummaryProjection::getServiceOrderStatus,
                                ServiceOrderStatusSummaryProjection::getTotal,
                                (existing, replacement) -> existing,
                                LinkedHashMap::new
                        ));


        return new OverviewReportDTO(
                revenue,
                totalServiceOrder,
                averageTicket,
                countPayment,
                distinctCustomers,
                revenueByDate,
                paymentbyMethod,
                serviceOrderStatusLongMap,
                services
        );
    }

    public FinancialReportDTO getFinancialReport(LocalDate startDate, LocalDate endDate) {
        Company company = getCurrentCompany();

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        BigDecimal revenue = paymentRepository.sumRevenueByPeriod(company, startDateTime, endDateTime, PaymentStatus.PAID);
        Long countPayment = paymentRepository.countByPeriod(company, startDateTime, endDateTime, PaymentStatus.PAID);
        Long paidServiceOrders = paymentRepository.countDistinctPaidServiceOrdersByPeriod(company, startDateTime, endDateTime, PaymentStatus.PAID );
        BigDecimal averageTicket = calculateAverageTicket(paidServiceOrders, revenue);
        Map<PaymentMethod, BigDecimal>  paymentByMethod = getSumPaymentMethod(company, startDateTime, endDateTime, PaymentStatus.PAID);
        List<FinancialDashboardGroupByDateDTO> revenueGroupedByDate = getRevenueGroupedByDate(company, PaymentStatus.PAID, startDateTime, endDateTime);



        return new FinancialReportDTO(
                revenue,
                countPayment,
                averageTicket,
                paymentByMethod,
                revenueGroupedByDate
        );
    }


    public Company getCurrentCompany() {
        return authenticatedUserService.getAuthenticatedUser().getCompany();
    }

    private BigDecimal calculateAverageTicket(Long countPayment, BigDecimal revenue) {
        if(countPayment == 0 || revenue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return revenue.divide(BigDecimal.valueOf(countPayment), 2, RoundingMode.HALF_UP);
    }

    private Map<PaymentMethod, BigDecimal> getSumPaymentMethod(Company company, LocalDateTime startDateTime, LocalDateTime endDateTime, PaymentStatus paymentStatus) {

        List<PaymentMethodSummaryProjection> paymentMethods = paymentRepository.findTotalAmountGroupedByMethod(company, startDateTime, endDateTime, paymentStatus);

        return paymentMethods.stream()
                .collect(Collectors.toMap(
                        PaymentMethodSummaryProjection::getPaymentMethod,
                        PaymentMethodSummaryProjection::getTotalAmount,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }

    private List<FinancialDashboardGroupByDateDTO> getRevenueGroupedByDate(Company company, PaymentStatus paymentStatus, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return paymentRepository.findAllByCompanyAndStatusAndCreatedAtBetweenGroupByCreatedAt(company, paymentStatus, startDateTime, endDateTime);
    }

    public Page<PaymentListResponseDTO> findAllPayments(
            PaymentMethod paymentMethod,
            PaymentStatus paymentStatus,
            String search,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        Company company = getCurrentCompany();

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        Page<PaymentListProjection> payments = paymentRepository.findAllByCompanyAndFilters(
                company,
                paymentMethod,
                paymentStatus,
                search,
                startDateTime,
                endDateTime,
                pageable
        );
        return payments.map(paymentMapper::fromProjection);
    }

    public OperationalReportDTO getOperationalReport(LocalDate startDate, LocalDate endDate) {

        Company company = getCurrentCompany();

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        ServiceOrderSummaryDTO serviceOrderSummary = getServiceOrderSummary(startDateTime, endDateTime);

        Long totalServiceOrder = serviceOrderSummary.totalServiceOrder();

        Long delivered = serviceOrderRepository.countOrders(company, ServiceOrderStatus.DELIVERED, startDateTime, endDateTime);

        Long canceled = serviceOrderRepository.countOrders(company, ServiceOrderStatus.CANCELLED, startDateTime, endDateTime);
        Long inProgress = serviceOrderSummary.totalServiceOrderInAction();

        List<BestSellingServiceDTO> bestSellingService = serviceOrderItemRepository.findBestSellingService(company, startDateTime, endDateTime, PaymentStatus.PAID);


        return new OperationalReportDTO(
                serviceOrderSummary,
                new ServiceOrderKpiDTO(totalServiceOrder, delivered, canceled, inProgress),
                bestSellingService

        );
    }

    @Transactional(readOnly = true)
    public Page<ServiceOrderProResponseDTO> findAllServiceOrders(
            ServiceOrderStatus status,
            String customer,
            String plate,
            LocalDate startDate,
            LocalDate endDate,
            String search,
            Pageable pageable
    ) {
        Company company = getCurrentCompany();

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);


        Page<ServiceOrderProjection> projections = serviceOrderRepository
                .findAllByCompanyAndFilters(company, status, customer, plate, startDateTime, endDateTime, search, PaymentStatus.PAID, pageable);

        return projections.map(projection -> serviceOrderMapper.fromEntityR(
                projection.getServiceOrder(),
                projection.getIsPaid()
        ));
    }

    private ServiceOrderSummaryDTO getServiceOrderSummary(LocalDateTime startDate, LocalDateTime endDate) {
        Company company = getCurrentCompany();

        Long waiting = serviceOrderRepository.countOrders(company, ServiceOrderStatus.WAITING, startDate, endDate);

        Long inProgress = serviceOrderRepository.countOrders(company, ServiceOrderStatus.IN_PROGRESS, startDate, endDate);

        Long ready = serviceOrderRepository.countOrders(company, ServiceOrderStatus.READY, startDate, endDate);

        Long delivered = serviceOrderRepository.countOrders(company, ServiceOrderStatus.DELIVERED, startDate, endDate);

        Long canceled = serviceOrderRepository.countOrders(company, ServiceOrderStatus.CANCELLED, startDate, endDate);

        return new ServiceOrderSummaryDTO(
                waiting,
                inProgress,
                ready,
                delivered,
                canceled
        );
    }

    public CustomerReportDTO getCustomerReport(LocalDate startDate, LocalDate endDate) {
        Company company = getCurrentCompany();

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        Long distinctCustomers = paymentRepository.countDistinctCustomersByPeriod(company, startDateTime, endDateTime, PaymentStatus.PAID);
        Long newCustomers = customerRepository.countByPeriod(company, startDateTime, endDateTime);

        List<CustomerProjection> customersMetrics = serviceOrderRepository.countRepeat(company, ServiceOrderStatus.CANCELLED, PaymentStatus.PAID,startDateTime, endDateTime);

        return new CustomerReportDTO(
                distinctCustomers,
                newCustomers,
                customersMetrics
        );
    }

    @Transactional(readOnly = true)
    public Page<CustomerMetricsDTO> getCustomerMetricsReport(
            String search,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        Company company = getCurrentCompany();

        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

        Page<CustomerProjection> projectionsPage = serviceOrderRepository.findAllByCustomerMetrics(company, ServiceOrderStatus.CANCELLED, PaymentStatus.PAID, search, startDateTime, endDateTime, pageable);

        return projectionsPage.map(proj -> new CustomerMetricsDTO(
                proj.getCustomerId(),
                proj.getCustomerName(),
                proj.getCustomerPhone(),
                proj.getTotalOrders(),
                proj.getAmountPayment()
        ));
    }
}
