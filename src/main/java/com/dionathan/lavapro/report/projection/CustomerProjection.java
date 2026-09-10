package com.dionathan.lavapro.report.projection;

import java.math.BigDecimal;

public interface CustomerProjection {
    Long getCustomerId();
    String getCustomerName();
    String getCustomerPhone();
    Long getTotalOrders();
    BigDecimal getAmountPayment();
}
