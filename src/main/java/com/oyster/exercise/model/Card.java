package com.oyster.exercise.model;

import com.oyster.exercise.vo.MonetaryAmount;

import java.math.BigDecimal;
import java.util.*;

public class Card {
    private String identifier;
    private MonetaryAmount balance;
    SortedSet<Transaction> transactions = new TreeSet<>(Comparator.comparing(Transaction::getDate));

    public Card() {
        this.identifier = UUID.randomUUID().toString();
        this.balance = new MonetaryAmount(new BigDecimal("0.00"));
    }

    public void addTransaction(Transaction transaction) {
        switch (transaction.getType()) {
            case LOAD:
                if(this.transactions.add(transaction)) {
                    this.balance = creditAmount(transaction.getAmount());
                }
                break;
            case SWIPE_IN:
            case SWIPE_OUT:
                if(transactions.add(transaction)) {
                    this.balance = debitAmount(transaction.getAmount());
                }
                break;

            default:
                throw new IllegalArgumentException("Undefined transaction type.");
        }
    }

    public void removeTransaction(Transaction transaction) {
        if(transactions.remove(transaction)) {
            this.balance = creditAmount(transaction.getAmount());
        }
    }

    public SortedSet<Transaction> getTransactions() {
        return Collections.unmodifiableSortedSet(transactions);
    }

    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String toString() {
        return String.format("Identifier: %s Balance: %s", this.identifier, this.balance);
    }

    public MonetaryAmount getBalance() {
        return this.balance;
    }

    private MonetaryAmount creditAmount(final MonetaryAmount amount) {
        return  this.balance.add(amount);
    }

    private MonetaryAmount debitAmount(final MonetaryAmount amount) {
        return this.balance.subtract(amount);
    }
}
