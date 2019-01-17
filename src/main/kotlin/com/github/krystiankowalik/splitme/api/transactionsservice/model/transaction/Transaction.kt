package com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction

import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.split.Split
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "transaction")
data class Transaction(
        @javax.persistence.Id
        @Column(name = "transaction_id")
        val id: String="",

        @Column(name = "public_id")
        val publicId: String="",

        @Column(name = "transaction_date")
        val transactionDate: LocalDate=LocalDate.now(),

        @Column(name = "description")
        val description: String = "",

        @OneToOne
        @JoinColumn(name = "split_id", referencedColumnName = "split_id")
        val split: Split=Split("", listOf(),""),

        @Enumerated(EnumType.STRING)
        @Column(name = "transaction_type")
        val type: Type=Type.EXPENSE,

        @Column(name = "added_time")
        val added: LocalDateTime= LocalDateTime.now(),

        @Column(name = "added_by")
        val addedBy: String="",

        @Column(name = "last_modified_time")
        val lastModified: LocalDateTime= LocalDateTime.now(),

        @Column(name = "last_modified_by")
        val lastModifiedBy: String=""
) {


    @Transient
    var amount: BigDecimal =split.getTotalAmount()
        get() = split.getTotalAmount()

    @Transient
    var currencyCode: String = split.currency
        get() = split.currency



    enum class Type(val factor: BigDecimal) {
        INCOME(BigDecimal.ONE), EXPENSE(INCOME.factor.negate())
    }
}