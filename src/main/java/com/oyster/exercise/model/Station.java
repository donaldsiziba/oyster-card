package com.oyster.exercise.model;

import java.util.List;

public class Station {
    private final String name;
    private final List<Integer> zones;

    public Station(String name, List<Integer> zones) {
        this.name = name;
        this.zones = zones;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getZones() {
        return zones;
    }
}
