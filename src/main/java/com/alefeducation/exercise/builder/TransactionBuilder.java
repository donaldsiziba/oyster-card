package com.alefeducation.exercise.builder;

import com.alefeducation.exercise.model.Transaction;
import com.alefeducation.exercise.model.TransactionType;
import com.alefeducation.exercise.model.Trip;
import com.alefeducation.exercise.vo.MonetaryAmount;

import java.util.Date;

public class TransactionBuilder {
    private final Transaction transaction;

    private TransactionBuilder() {
        this.transaction = new Transaction();
        this.transaction.setDate(new Date());
    }

    public static TransactionBuilder createTransaction() {
        return new TransactionBuilder();
    }

    public TransactionBuilder amount(final MonetaryAmount amount) {
        this.transaction.setAmount(amount);
        return this;
    }

    public TransactionBuilder type(final TransactionType type) {
        this.transaction.setType(type);
        return this;
    }

    public TransactionBuilder trip(final Trip trip) {
        this.transaction.setTrip(trip);
        return this;
    }

    public Transaction build() {
        return this.transaction;
    }
}
