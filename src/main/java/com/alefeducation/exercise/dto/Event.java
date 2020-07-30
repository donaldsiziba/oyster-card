package com.alefeducation.exercise.dto;

import com.alefeducation.exercise.model.TransactionType;
import com.alefeducation.exercise.model.TripType;

public class Event {
    private final String identifier;
    private final TransactionType transactionType;
    private final TripType tripType;
    private final String station;

    public Event(String identifier, TransactionType transactionType, TripType tripType, String station) {
        this.identifier = identifier;
        this.transactionType = transactionType;
        this.tripType = tripType;
        this.station = station;
    }

    public String getIdentifier() {
        return identifier;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public TripType getTripType() {
        return tripType;
    }

    public String getStation() {
        return station;
    }
}
