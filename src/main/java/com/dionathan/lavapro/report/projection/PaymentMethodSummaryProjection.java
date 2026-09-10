package com.dionathan.lavapro.report.projection;

import com.dionathan.lavapro.payment.PaymentMethod;

import java.math.BigDecimal;

public interface PaymentMethodSummaryProjection {
    PaymentMethod getPaymentMethod();

    BigDecimal getTotalAmount();
}
