package com.alefeducation.exercise.dao;

import com.alefeducation.exercise.model.Card;
import com.alefeducation.exercise.store.InMemoryCardStore;

public class CardDAO {
    InMemoryCardStore store = InMemoryCardStore.INSTANCE;

    public void persist(Card card) {
        store.getCards().put(card.getIdentifier(), card);
    }

    public Card find(String identifier) {
        return store.getCards().get(identifier);
    }
}
