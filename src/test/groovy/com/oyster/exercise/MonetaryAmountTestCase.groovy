package com.oyster.exercise

import com.oyster.exercise.vo.MonetaryAmount
import spock.lang.Specification

class MonetaryAmountTestCase extends Specification {
    def "add monetary amounts"() {
        expect:
            new MonetaryAmount(new BigDecimal("5.00")).add(new MonetaryAmount(new BigDecimal("5.00"))).getAmount() ==
                    new MonetaryAmount(new BigDecimal("10.00")).getAmount()
    }

    def "subtract monetary amounts"() {
        expect:
            new MonetaryAmount(new BigDecimal("10.00")).subtract(new MonetaryAmount(new BigDecimal("5.00"))).getAmount() ==
                    new MonetaryAmount(new BigDecimal("5.00")).getAmount()
    }
}
