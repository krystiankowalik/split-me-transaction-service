package com.github.krystiankowalik.splitme.api.transactionsservice.model.due

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.krystiankowalik.splitme.api.transactionsservice.model.money.Money
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "due")
data class Due(@JsonIgnore @Id val id: String,
               val publicId: String,
               val debtorId: String,
               val creditorId: String,
               val money: Money,
               val settled: Boolean,
               val transactionId: String
) {

}

