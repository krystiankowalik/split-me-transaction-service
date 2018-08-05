package com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.krystiankowalik.splitme.api.transactionsservice.model.money.Money
import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.split.Split
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "transaction")
data class Transaction(@Id @JsonIgnore val id: String,
                       val publicId: String,
                       val date: LocalDate,
                       val description: String="",
                       val money: Money,
                       val type: Type,
                       val split: Split,
                       val added: LocalDateTime,
                       val addedBy: String,
                       val lastModified: LocalDateTime,
                       val lastModifiedBy: String
) {

    enum class Type(val factor: BigDecimal) {
        INCOME(BigDecimal.ONE), EXPENSE(INCOME.factor.negate())
    }
}
