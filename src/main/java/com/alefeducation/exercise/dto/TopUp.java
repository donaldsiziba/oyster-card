package com.alefeducation.exercise.dto;

import com.alefeducation.exercise.vo.MonetaryAmount;

public class TopUp {
    private final String identifier;
    private final MonetaryAmount amount;

    public TopUp(final String identifier, final MonetaryAmount amount) {
        this.identifier = identifier;
        this.amount = amount;
    }

    public String getIdentifier() {
        return identifier;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }
}
