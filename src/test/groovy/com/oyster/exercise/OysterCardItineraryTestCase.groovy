package com.oyster.exercise


import com.oyster.exercise.dto.Event
import com.oyster.exercise.dto.TopUp
import com.oyster.exercise.hardware.AccessGate
import com.oyster.exercise.hardware.TicketVendingMachine
import com.oyster.exercise.model.Card
import com.oyster.exercise.model.TransactionType
import com.oyster.exercise.model.TripType
import com.oyster.exercise.vo.MonetaryAmount
import spock.lang.Specification

class OysterCardItineraryTestCase extends Specification  {
    TicketVendingMachine tvm

    def setup() {
        tvm = new TicketVendingMachine()
    }

    def "oyster card example itinerary"() {
        given: "a customer's card is loaded with £30.00"
            Card card = tvm.dispense()
            tvm.topUp(new TopUp(card.getIdentifier(), new MonetaryAmount(new BigDecimal("30.00"))))
            Thread.sleep(1l)

        when: "the customer uses the tube to travel from Holborn to Earl's Court"
            new AccessGate().register(new Event(card.getIdentifier(), TransactionType.SWIPE_IN, TripType.TUBE, "Holborn"))
            Thread.sleep(1l)
            new AccessGate().register(new Event(card.getIdentifier(), TransactionType.SWIPE_OUT, TripType.TUBE, "Earl's Court"))
            Thread.sleep(1l)

        and: "the customer catches a bus from Earl's Court to Chelsea"
            new AccessGate().register(new Event(card.getIdentifier(), TransactionType.SWIPE_IN, TripType.BUS, "Earl's Court"))
            Thread.sleep(1l)

        and: "the customer rides the tube from Earl's Court to Hammersmith"
            new AccessGate().register(new Event(card.getIdentifier(), TransactionType.SWIPE_IN, TripType.TUBE, "Earl's Court"))
            Thread.sleep(1l)
            Card exit = new AccessGate().register(new Event(card.getIdentifier(), TransactionType.SWIPE_OUT, TripType.TUBE, "Hammersmith"))

        then: "the balance on the customer's card should be £23.70"
            exit.getBalance().toString() == "£23.70"
    }
}
