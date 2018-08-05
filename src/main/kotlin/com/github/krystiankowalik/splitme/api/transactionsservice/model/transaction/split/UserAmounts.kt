package com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.split

import java.math.BigDecimal

data class UserAmounts(val userId: String,
                  val exchanged: BigDecimal,
                  val share: BigDecimal) {


}

