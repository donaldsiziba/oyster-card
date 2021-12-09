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

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class BusItineraryTestCase extends Specification {
    TicketVendingMachine tvm

    def setup() {
        tvm = new TicketVendingMachine()
    }

    @Unroll("calculate the correct card balance for a bus trip from #departure to #destination")
    def "calculate the correct card balance for a bus trip from #departure to #destination"() {
        given: "a customer's card is loaded with £#amount"
            Card card = tvm.dispense()
            tvm.topUp(new TopUp(card.getIdentifier(), new MonetaryAmount(new BigDecimal(amount))))
            new CountDownLatch(1).await(1l, TimeUnit.NANOSECONDS)
        when: "the customer swipes into the bus at #departure"
            Card entry = new AccessGate().register(new Event(card.getIdentifier(), TransactionType.SWIPE_IN, TripType.valueOf(mode), departure))

        then: "the balance on the customer's card should be #interimBalance"
            entry.getBalance().toString() == interimBalance

        and: "when the customer drops off at #destination the balance on the card should still be #interimBalance"
            Card exit = new AccessGate().register(new Event(card.getIdentifier(), TransactionType.SWIPE_OUT, TripType.valueOf(mode), destination))

        and: "then the balance on the card should still be #interimBalance"
            exit.getBalance().toString() == finalBalance
        where:
            amount   | initialEvent | mode   | departure       | interimBalance  | destination || finalBalance
            "10.00"  | "SWIPE_IN"   | "BUS"  |  "Earl's Court" | "£8.20"         | "Chelsea"   || "£8.20"
    }
}
