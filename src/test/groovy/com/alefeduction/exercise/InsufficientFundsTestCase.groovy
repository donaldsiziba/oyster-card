package com.alefeduction.exercise


import com.alefeducation.exercise.dto.Event
import com.alefeducation.exercise.dto.TopUp
import com.alefeducation.exercise.exception.InsufficientFundsException
import com.alefeducation.exercise.hardware.AccessGate
import com.alefeducation.exercise.hardware.TicketVendingMachine
import com.alefeducation.exercise.model.Card
import com.alefeducation.exercise.model.TransactionType
import com.alefeducation.exercise.model.TripType
import com.alefeducation.exercise.vo.MonetaryAmount
import spock.lang.Specification
import spock.lang.Unroll

class InsufficientFundsTestCase extends Specification {
    TicketVendingMachine tvm
    AccessGate gate

    def setup() {
        tvm = new TicketVendingMachine()
        gate = new AccessGate()
    }

    @Unroll("Customer tries to board #mode with a balance of £#amount on their card to #departure")
    def "card with insufficient balance"() {
        given: "a card with a balance of £#amount"
            Card card = tvm.dispense()
            tvm.topUp(new TopUp(card.getIdentifier(), new MonetaryAmount(new BigDecimal(amount))))

        when: "the customer attempts to board the #mode with this card from #departure"
            gate.register(new Event(card.getIdentifier(), TransactionType.SWIPE_IN, TripType.valueOf(mode), departure))

        then: "the customer should be made aware that their card has an insufficient balance"
            InsufficientFundsException e = thrown()
            e.getMessage() == "Current balance of £${amount} is less than the required minimum amount of ${minimunRequiredAmount}"

        where:
            amount  | event      | mode   | minimunRequiredAmount | departure
            "2.00"  | "SWIPE_IN" | "TUBE" | "£3.20"               | "Earl's Court"
            "1.00"  | "SWIPE_IN" | "BUS"  | "£1.80"               | "Wimbledon"
    }
}
