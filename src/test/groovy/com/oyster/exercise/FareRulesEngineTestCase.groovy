package com.oyster.exercise

import com.oyster.exercise.builder.TripBuilder
import com.oyster.exercise.model.Trip
import com.oyster.exercise.model.TripType
import com.oyster.exercise.rules.FareRulesEngine
import com.oyster.exercise.vo.MonetaryAmount
import spock.lang.Specification
import spock.lang.Unroll

class FareRulesEngineTestCase extends Specification {
    FareRulesEngine rulesEngine

    def setup() {
        rulesEngine = new FareRulesEngine()
    }

    @Unroll("#description")
    def "calculate the fare between #departure and #destination"() {
        given: "the a customer takes a #mode trip from #departure to #destination"
            Trip trip = TripBuilder.createTrip().ofType(TripType.valueOf(mode)).from(departure).to(destination).build()

        when: "the fare for this trip is calculated"
            MonetaryAmount amount = rulesEngine.apply(trip)

        then: "the fare should be #fare"
            amount.toString() == fare

        where:
            departure      | destination    | mode   || fare    | description
            "Holborn"      | "Earl's Court" | "TUBE" || "£2.50" | "Anywhere in zone 1"
            "Earl's Court" | "Hammersmith"  | "TUBE" || "£2.00" | "Any one zone outside zone 1"
            "Holborn"      | "Hammersmith"  | "TUBE" || "£3.00" | "Any two zones including zone 1"
            "Holborn"      | "Wimbledon"    | "TUBE" || "£3.20" | "Any two zones including zone 1"
            "Hammersmith"  | "Wimbledon"    | "TUBE" || "£2.25" | "Any two zones excluding zone 1"
            "Wimbledon"    | "Hammersmith"  | "TUBE" || "£2.25" | "Any two zones excluding zone 1"
            "Earl's Court" | "Wimbledon"    | "TUBE" || "£2.25" | "Any two zones excluding zone 1"
            "Holborn"      | "Wimbledon"    | "TUBE" || "£3.20" | "Any three zones"
            "Earl's Court" | "Chelsea"      | "BUS"  || "£1.80" | "Any bus trip"
    }
}
