package com.alefeducation.exercise.hardware;

import com.alefeducation.exercise.builder.TransactionBuilder;
import com.alefeducation.exercise.dao.CardDAO;
import com.alefeducation.exercise.dto.TopUp;
import com.alefeducation.exercise.model.Card;
import com.alefeducation.exercise.model.TransactionType;

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
