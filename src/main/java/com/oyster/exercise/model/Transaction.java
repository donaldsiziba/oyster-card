package com.oyster.exercise.model;

import com.oyster.exercise.vo.MonetaryAmount;

import java.util.Date;

public class Transaction {
    private MonetaryAmount amount;
    private Date date;
    private TransactionType type;
    private Trip trip;

    public MonetaryAmount getAmount() {
        return amount;
    }

    public void setAmount(MonetaryAmount amount) {
        this.amount = amount;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Date getDate() {
        return this.date;
    }
}
