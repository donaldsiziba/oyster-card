package com.oyster.exercise.vo;

import java.math.BigDecimal;

public class MonetaryAmount {
    private final String symbol = "£";
    private final BigDecimal amount;

    public MonetaryAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public MonetaryAmount add(MonetaryAmount other) {
        return new MonetaryAmount(this.amount.add(other.amount));
    }

    public MonetaryAmount subtract(MonetaryAmount other) {
        return new MonetaryAmount(this.amount.subtract(other.amount));
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    @Override
    public String toString() {
        return String.format("%s%s", this.symbol, this.amount);
    }
}
