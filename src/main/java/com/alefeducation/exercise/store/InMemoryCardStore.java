package com.alefeducation.exercise.store;

import com.alefeducation.exercise.model.Card;

import java.util.HashMap;
import java.util.Map;

public enum InMemoryCardStore {
    INSTANCE;

    Map<String, Card> cards = new HashMap<>();

    public Map<String, Card> getCards() {
        return cards;
    }
}
