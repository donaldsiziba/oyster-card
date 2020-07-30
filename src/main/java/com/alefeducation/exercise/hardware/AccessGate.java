package com.alefeducation.exercise.hardware;

import com.alefeducation.exercise.builder.TransactionBuilder;
import com.alefeducation.exercise.builder.TripBuilder;
import com.alefeducation.exercise.rules.FareRulesEngine;
import com.alefeducation.exercise.dao.CardDAO;
import com.alefeducation.exercise.dto.Event;
import com.alefeducation.exercise.exception.InsufficientFundsException;
import com.alefeducation.exercise.model.Card;
import com.alefeducation.exercise.model.Transaction;
import com.alefeducation.exercise.model.TransactionType;
import com.alefeducation.exercise.store.MaximumPossibleFareStore;
import com.alefeducation.exercise.vo.MonetaryAmount;

public class AccessGate {
    MaximumPossibleFareStore store;
    CardDAO dao;
    FareRulesEngine rulesEngine;

    public AccessGate() {
        store = new MaximumPossibleFareStore();
        dao = new CardDAO();
        rulesEngine = new FareRulesEngine();
    }

    public Card register(Event request) {
        Card card = dao.find(request.getIdentifier());

        switch(request.getTransactionType()) {
            case SWIPE_IN:
                Transaction entry = TransactionBuilder.createTransaction().type(TransactionType.SWIPE_IN).trip(TripBuilder.createTrip().ofType(request.getTripType()).from(request.getStation()).build()).build();
                final MonetaryAmount maximumFare = store.getAmounts().get(request.getTripType());
                if(card.getBalance().getAmount().compareTo(maximumFare.getAmount()) < 0) {
                    throw new InsufficientFundsException(String.format("Current balance of %s is less than the required minimum amount of %s", card.getBalance(), maximumFare));
                }
                entry.setAmount(maximumFare);
                card.addTransaction(entry);

                break;
            case SWIPE_OUT:
                Transaction lastTransaction = card.getTransactions().last();
                if(lastTransaction.getType() != TransactionType.SWIPE_IN) {
                    card.addTransaction(TransactionBuilder.createTransaction().type(TransactionType.SWIPE_OUT).amount(store.getAmounts().get(request.getTripType())).build());
                } else {
                    Transaction exit = TransactionBuilder.createTransaction().type(TransactionType.SWIPE_OUT).trip(TripBuilder.createTrip().ofType(request.getTripType()).to(request.getStation()).build()).build();
                    exit.getTrip().setDeparture(lastTransaction.getTrip().getDeparture());
                    card.removeTransaction(lastTransaction);
                    exit.setAmount(rulesEngine.apply(exit.getTrip()));

                    card.addTransaction(exit);
                }
                break;

            default:
                throw new IllegalArgumentException("Undefined transaction type.");
        }
        dao.persist(card);
        return card;
    }
}
