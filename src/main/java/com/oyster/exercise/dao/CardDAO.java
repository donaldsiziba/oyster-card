package com.oyster.exercise.dao;

import com.oyster.exercise.model.Card;
import com.oyster.exercise.store.InMemoryCardStore;

public class CardDAO {
    InMemoryCardStore store = InMemoryCardStore.INSTANCE;

    public void persist(Card card) {
        store.getCards().put(card.getIdentifier(), card);
    }

    public Card find(String identifier) {
        return store.getCards().get(identifier);
    }
}
