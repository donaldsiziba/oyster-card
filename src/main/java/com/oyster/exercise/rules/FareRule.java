package com.oyster.exercise.rules;

import com.oyster.exercise.model.Trip;
import com.oyster.exercise.vo.MonetaryAmount;

import java.util.function.Predicate;

public interface FareRule extends Predicate<Trip> {
    MonetaryAmount getAmount();
}
