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
import spock.lang.Unroll

class TubeItineraryTestCase extends Specification {
    TicketVendingMachine tvm

    def setup() {
        tvm = new TicketVendingMachine()
    }

    @Unroll("calculate the correct card balance for a trip from #departure to #destination")
    def "calculate the correct card balance for a trip from #departure to #destination"() {
        given: "a customer's card is loaded with £#amount"
            Card card = tvm.dispense()
            tvm.topUp(new TopUp(card.getIdentifier(), new MonetaryAmount(new BigDecimal(amount))))
            Thread.sleep(1l)

        when: "the customer swipes in onto the #mode from #departure"
            Card entry = new AccessGate().register(new Event(card.getIdentifier(), TransactionType.SWIPE_IN, TripType.valueOf(mode), departure))

        then: "the balance on the customer's card should be #interimBalance"
            entry.getBalance().toString() == interimBalance
            Thread.sleep(1l)

        and: "when the customer swipes out at #destination"
            Card exit = new AccessGate().register(new Event(card.getIdentifier(), TransactionType.SWIPE_OUT, TripType.valueOf(mode), destination))

        and: "then the customer's card should have a balance of #finalBalance"
            exit.getBalance().toString() == finalBalance
            exit.getTransactions().size() == 2

        where:
            amount  | initialEvent | mode   | departure       | interimBalance  | nextEvent   | destination    || finalBalance
            "5.00"  | "SWIPE_IN"   | "TUBE" |  "Holborn"      | "£1.80"         | "SWIPE_OUT" | "Earl's Court" || "£2.50"
            "6.00"  | "SWIPE_IN"   | "TUBE" |  "Earl's Court" | "£2.80"         | "SWIPE_OUT" | "Hammersmith"  || "£4.00"
            "7.00"  | "SWIPE_IN"   | "TUBE" |  "Holborn"      | "£3.80"         | "SWIPE_OUT" | "Hammersmith"  || "£4.00"
            "8.00"  | "SWIPE_IN"   | "TUBE" |  "Holborn"      | "£4.80"         | "SWIPE_OUT" | "Wimbledon"    || "£4.80"
            "9.00"  | "SWIPE_IN"   | "TUBE" |  "Earl's Court" | "£5.80"         | "SWIPE_OUT" | "Wimbledon"    || "£6.75"
            "10.00" | "SWIPE_IN"   | "TUBE" |  "Hammersmith"  | "£6.80"         | "SWIPE_OUT" | "Wimbledon"    || "£7.75"
    }
}
