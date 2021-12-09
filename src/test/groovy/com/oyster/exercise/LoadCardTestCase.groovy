package com.oyster.exercise


import com.oyster.exercise.dto.TopUp
import com.oyster.exercise.hardware.TicketVendingMachine
import com.oyster.exercise.model.Card
import com.oyster.exercise.vo.MonetaryAmount
import spock.lang.Specification
import spock.lang.Unroll

class LoadCardTestCase extends Specification {
    TicketVendingMachine tvm

    def setup() {
        tvm = new TicketVendingMachine()
    }

    @Unroll("Card is loaded with £#amount")
    def "load card with case"() {
        given: "a customer's card"
            Card card = tvm.dispense()

        when: "the card is loaded with £#amount"
            Card response = tvm.topUp(new TopUp(card.getIdentifier(), new MonetaryAmount(new BigDecimal(amount))))

        then: "the balance on the customer's card is #newBalance"
            response.getBalance().toString() == newBalance

        where:
            amount  | event   || newBalance
            "5.00"  | "LOAD"  || "£5.00"
            "10.00" | "LOAD"  || "£10.00"
    }
}
