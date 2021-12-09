package com.oyster.exercise.rules;

import com.oyster.exercise.model.Trip;
import com.oyster.exercise.model.TripType;
import com.oyster.exercise.store.MaximumPossibleFareStore;
import com.oyster.exercise.vo.MonetaryAmount;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class FareRulesEngine implements Function<Trip, MonetaryAmount> {
    private final Integer zoneOne = 1;

    ToIntFunction<Trip> minimumDifference = trip ->  trip.getDeparture().getZones().stream()
            .flatMapToInt(n -> trip.getDestination().getZones().stream()
                    .mapToInt(r -> Math.abs(r - n))).min().getAsInt();

    ToIntFunction<Trip> maximumDifference = trip -> trip.getDeparture().getZones().stream()
            .flatMapToInt(n -> trip.getDestination().getZones().stream()
                    .mapToInt(r -> Math.abs(r - n))).max().getAsInt();

    Predicate<Trip> anyBusTrip = trip -> trip.getType() == TripType.BUS;

    Predicate<Trip> withinTheSameZone = trip ->  minimumDifference.applyAsInt(trip) == 0;

    Predicate<Trip> anyTwoZones = trip ->  minimumDifference.applyAsInt(trip) == 1 || maximumDifference.applyAsInt(trip) == 1;

    Predicate<Trip> withinZoneOne = trip -> trip.getDeparture().getZones().contains(zoneOne) && trip.getDestination().getZones().contains(zoneOne);

    Predicate<Trip> includesZoneOne = trip ->  (trip.getDeparture().getZones().size() == 1 && trip.getDeparture().getZones().contains(zoneOne)) ||
                                                    (trip.getDestination().getZones().size() == 1 && trip.getDestination().getZones().contains(zoneOne));

    Predicate<Trip> anyWhereInZoneOne = withinTheSameZone.and(withinZoneOne);

    Predicate<Trip> anyOneZoneOutsideZoneOne = withinTheSameZone.and(withinZoneOne.negate());

    Predicate<Trip> anyTwoZonesIncludingZoneOne = anyTwoZones.and(includesZoneOne);

    Predicate<Trip> anyTwoZonesExcludingZoneOne = anyTwoZones.and(includesZoneOne.negate());

    Predicate<Trip> anyThreeZones = trip -> maximumDifference.applyAsInt(trip) == 2;

    private List<FareRule> rules = Arrays.asList(new ZoneRule[] { new ZoneRule(anyWhereInZoneOne, new MonetaryAmount(new BigDecimal("2.50"))),
                                                                  new ZoneRule(anyOneZoneOutsideZoneOne, new MonetaryAmount(new BigDecimal("2.00"))),
                                                                  new ZoneRule(anyTwoZonesIncludingZoneOne, new MonetaryAmount(new BigDecimal("3.00"))),
                                                                  new ZoneRule(anyTwoZonesExcludingZoneOne, new MonetaryAmount(new BigDecimal("2.25"))),
                                                                  new ZoneRule(anyThreeZones, new MaximumPossibleFareStore().getAmounts().get(TripType.TUBE))
                                                                });

    @Override
    public MonetaryAmount apply(Trip trip) {
        if(anyBusTrip.test(trip)) {
            return new MaximumPossibleFareStore().getAmounts().get(TripType.BUS);
        }

        return rules.stream()
                    .filter(p -> p.test(trip))
                    .map(FareRule::getAmount).collect(Collectors.toList())
                    .stream()
                    .min(Comparator.comparing(MonetaryAmount::getAmount)).get();
    }
}