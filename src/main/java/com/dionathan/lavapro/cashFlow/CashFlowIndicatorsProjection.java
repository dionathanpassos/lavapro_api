package com.dionathan.lavapro.cashFlow;

import java.math.BigDecimal;

public interface CashFlowIndicatorsProjection {
    BigDecimal getIncome();
    BigDecimal getExpense();
}
