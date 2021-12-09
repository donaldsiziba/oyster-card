package com.oyster.exercise.hardware;

import com.oyster.exercise.builder.TransactionBuilder;
import com.oyster.exercise.dao.CardDAO;
import com.oyster.exercise.dto.TopUp;
import com.oyster.exercise.model.Card;
import com.oyster.exercise.model.TransactionType;

public class TicketVendingMachine {
    private CardDAO dao;

    public TicketVendingMachine() {
        dao = new CardDAO();
    }

    public Card dispense() {
        Card card = new Card();
        dao.persist(card);
        return card;
    }

    public Card topUp(TopUp request) {
        Card card = dao.find(request.getIdentifier());
        card.addTransaction(TransactionBuilder.createTransaction().amount(request.getAmount()).type(TransactionType.LOAD).build());
        dao.persist(card);
        return card;
    }
}
