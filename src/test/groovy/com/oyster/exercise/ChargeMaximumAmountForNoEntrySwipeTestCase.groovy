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

class ChargeMaximumAmountForNoEntrySwipeTestCase extends Specification {
    TicketVendingMachine tvm

    def setup() {
        tvm = new TicketVendingMachine()
    }

    def "charge maximum amount if user did not swipe in"() {
        given: "a customer's card is loaded with £5.00"
            Card card = tvm.dispense()
            tvm.topUp(new TopUp(card.getIdentifier(), new MonetaryAmount(new BigDecimal("5.00"))))
            Thread.sleep(1l)

        when: "the customer does not swipe in for the tube trip"

        and: "when the customer swipes out of the destination station"
            Card exit = new AccessGate().register(new Event(card.getIdentifier(), TransactionType.SWIPE_OUT, TripType.TUBE, "Hammersmit"))

        then: " the customer should be charged the maximum tube trip"
            exit.getBalance().toString() == "£1.80"
    }
}
