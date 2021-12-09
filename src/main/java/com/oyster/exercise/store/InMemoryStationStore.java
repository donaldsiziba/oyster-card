package com.oyster.exercise.store;

import com.oyster.exercise.model.Station;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InMemoryStationStore {
    private Map<String, Station> stations = new HashMap();

    public InMemoryStationStore() {
        this.stations.put("Holborn", new Station("Holborn", Arrays.asList(1)));
        this.stations.put("Earl's Court", new Station("Earl's Court", Arrays.asList(1, 2)));
        this.stations.put("Hammersmith", new Station("Hammersmith", Arrays.asList(2)));
        this.stations.put("Wimbledon", new Station("Wimbledon", Arrays.asList(3)));
        this.stations.put("Chelsea", new Station("Chelsea", Arrays.asList(2)));  /* Assumption: Chelsea is in Zone since the requirements
                                                                                            gives an example of a bus ride from Earl's Court to Chelsea
                                                                                        */
    }

    public Map<String, Station> getStations() {
        return stations;
    }
}
