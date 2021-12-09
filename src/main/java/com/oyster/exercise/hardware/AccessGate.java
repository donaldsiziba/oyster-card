package com.oyster.exercise.hardware;

import com.oyster.exercise.builder.TransactionBuilder;
import com.oyster.exercise.builder.TripBuilder;
import com.oyster.exercise.model.Trip;
import com.oyster.exercise.rules.FareRulesEngine;
import com.oyster.exercise.dao.CardDAO;
import com.oyster.exercise.dto.Event;
import com.oyster.exercise.exception.InsufficientFundsException;
import com.oyster.exercise.model.Card;
import com.oyster.exercise.model.Transaction;
import com.oyster.exercise.model.TransactionType;
import com.oyster.exercise.store.MaximumPossibleFareStore;
import com.oyster.exercise.vo.MonetaryAmount;

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
                    final Trip trip = TripBuilder.createTrip()
                            .ofType(request.getTripType())
                            .from(lastTransaction.getTrip().getDeparture().getName())
                            .to(request.getStation()).build();

                    card.removeTransaction(lastTransaction);

                    card.addTransaction(TransactionBuilder.createTransaction().type(TransactionType.SWIPE_OUT).trip(trip).amount(rulesEngine.apply(trip)).build());
                }
                break;

            default:
                throw new IllegalArgumentException("Undefined transaction type.");
        }
        dao.persist(card);
        return card;
    }
}
