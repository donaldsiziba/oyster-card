package com.oyster.exercise.rules;

import com.oyster.exercise.model.Trip;
import com.oyster.exercise.vo.MonetaryAmount;

import java.util.function.Predicate;

public class ZoneRule implements FareRule {
    private final Predicate<Trip> predicate;
    private final MonetaryAmount amount;

    public ZoneRule(final Predicate<Trip> predicate, final MonetaryAmount amount) {
        this.predicate = predicate;
        this.amount = amount;
    }

    @Override
    public MonetaryAmount getAmount() {
        return this.amount;
    }

    @Override
    public boolean test(Trip trip) {
        return predicate.test(trip);
    }
}
