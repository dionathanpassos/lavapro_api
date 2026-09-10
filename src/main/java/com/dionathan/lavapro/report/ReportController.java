package com.dionathan.lavapro.report;

import com.dionathan.lavapro.payment.PaymentMethod;
import com.dionathan.lavapro.payment.PaymentStatus;
import com.dionathan.lavapro.payment.dto.PaymentListResponseDTO;
import com.dionathan.lavapro.report.dto.*;
import com.dionathan.lavapro.serviceOrder.ServiceOrderStatus;
import com.dionathan.lavapro.serviceOrder.dto.ServiceOrderProResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/overview")
    public ResponseEntity<OverviewReportDTO> getOverviewReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate
    ) {
        OverviewReportDTO revenue = reportService.getOverviewReport(startDate, endDate);

        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/financial")
    public ResponseEntity<FinancialReportDTO> getFinancialReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate
    ) {
        FinancialReportDTO financialReport = reportService.getFinancialReport(startDate, endDate);

        return ResponseEntity.ok(financialReport);
    }

    @GetMapping("/financial/payment")
    public ResponseEntity<Page<PaymentListResponseDTO>> findAll(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @RequestParam(required = false) PaymentStatus paymentStatus,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate
    ) {
        Page<PaymentListResponseDTO> payments = reportService.findAllPayments(paymentMethod, paymentStatus, search, startDate, endDate, pageable);

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/operational")
    public ResponseEntity<OperationalReportDTO> getOperationalReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate
    ) {
        OperationalReportDTO operationalReport = reportService.getOperationalReport(startDate, endDate);

        return ResponseEntity.ok(operationalReport);
    }

    @GetMapping("/operational/service-orders")
    public ResponseEntity<Page<ServiceOrderProResponseDTO>> findAll(
            @RequestParam(required = false) ServiceOrderStatus status,
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String search,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ServiceOrderProResponseDTO> serviceOrders = reportService.findAllServiceOrders(status, customer, plate, startDate, endDate, search, pageable);

        return ResponseEntity.ok(serviceOrders);
    }

    @GetMapping("/customer")
    public ResponseEntity<CustomerReportDTO> getCustomerReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate
    ) {
        CustomerReportDTO customerReport = reportService.getCustomerReport(startDate, endDate);

        return ResponseEntity.ok(customerReport);
    }

    @GetMapping("/customer/metrics")
    public ResponseEntity<Page<CustomerMetricsDTO>> getCustomerMetricsReport(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate,
            @PageableDefault(sort = "amountPayment", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<CustomerMetricsDTO> customerMetricsReport = reportService.getCustomerMetricsReport(search, startDate, endDate, pageable);

        return ResponseEntity.ok(customerMetricsReport);
    }
}
