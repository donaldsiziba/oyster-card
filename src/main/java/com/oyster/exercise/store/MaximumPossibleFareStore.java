package com.oyster.exercise.store;

import com.oyster.exercise.model.TripType;
import com.oyster.exercise.vo.MonetaryAmount;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MaximumPossibleFareStore {
    private Map<TripType, MonetaryAmount> amounts = new HashMap<>();

    public MaximumPossibleFareStore() {
        amounts.put(TripType.BUS, new MonetaryAmount(new BigDecimal("1.80")));
        amounts.put(TripType.TUBE, new MonetaryAmount(new BigDecimal("3.20")));
    }

    public Map<TripType, MonetaryAmount> getAmounts() {
        return amounts;
    }
}
