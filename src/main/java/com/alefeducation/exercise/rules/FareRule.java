package com.alefeducation.exercise.rules;

import com.alefeducation.exercise.model.Trip;
import com.alefeducation.exercise.vo.MonetaryAmount;

import java.util.function.Predicate;

public interface FareRule extends Predicate<Trip> {
    MonetaryAmount getAmount();
}
