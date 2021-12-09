package com.oyster.exercise.builder;

import com.oyster.exercise.model.Station;
import com.oyster.exercise.model.Trip;
import com.oyster.exercise.model.TripType;
import com.oyster.exercise.store.InMemoryStationStore;

public class TripBuilder {
    private final Trip trip;
    private final InMemoryStationStore stationStore;

    private TripBuilder() {
        this.trip = new Trip();
        this.stationStore = new InMemoryStationStore();
    }

    public static TripBuilder createTrip() {
        return new TripBuilder();
    }

    public TripBuilder ofType(final TripType type) {
        this.trip.setType(type);
        return this;
    }

    public TripBuilder from(final String station) {
        this.trip.setDeparture(find(station));
        return this;
    }

    public TripBuilder to(final String station) {
        this.trip.setDestination(find(station));
        return this;
    }

    public Trip build() {
        return this.trip;
    }

    private Station find(String station) {
        return this.stationStore.getStations().get(station);
    }
}
