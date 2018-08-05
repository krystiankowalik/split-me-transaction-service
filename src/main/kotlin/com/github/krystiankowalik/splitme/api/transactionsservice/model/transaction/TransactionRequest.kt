package com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction

import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.split.Split
import java.time.LocalDate

data class TransactionRequest(val publicId: String = "",
                              val date: LocalDate,
                              val description: String,
                              val split: Split)