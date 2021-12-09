package com.oyster.exercise.store;

import com.oyster.exercise.model.Card;

import java.util.HashMap;
import java.util.Map;

public enum InMemoryCardStore {
    INSTANCE;

    Map<String, Card> cards = new HashMap<>();

    public Map<String, Card> getCards() {
        return cards;
    }
}
