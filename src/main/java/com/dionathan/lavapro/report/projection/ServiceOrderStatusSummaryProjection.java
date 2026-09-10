package com.dionathan.lavapro.report.projection;

import com.dionathan.lavapro.payment.PaymentMethod;
import com.dionathan.lavapro.serviceOrder.ServiceOrderStatus;

import java.math.BigDecimal;

public interface ServiceOrderStatusSummaryProjection {
    ServiceOrderStatus getServiceOrderStatus();
    Long getTotal();
}
