package com.dionathan.lavapro.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PaymentListProjection {
    Long getId();
    BigDecimal getAmount();
    PaymentMethod getPaymentMethod();
    PaymentStatus getPaymentStatus();
    LocalDateTime getPaidAt();
    Long getServiceOrderId();
    String getCustomerName();
    String getVehiclePlate();
    String getVehicleBrand();
    String getVehicleModel();
}
