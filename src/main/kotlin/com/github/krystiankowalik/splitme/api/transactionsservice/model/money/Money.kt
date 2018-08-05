package com.github.krystiankowalik.splitme.api.transactionsservice.model.money

import java.math.BigDecimal

data class Money(val amount: BigDecimal,
            val currency: Currency) {
}