package com.github.krystiankowalik.splitme.api.transactionsservice.model.due

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "due")
data class Due(@Id
               @Column(name = "due_id")
               val dueId: String,

               @Column(name = "public_id")
               val publicId: String,

               @Column(name = "debtor_id")
               val debtorId: String,

               @Column(name = "creditor_id")
               val creditorId: String,

               @Column(name = "amount")
               val amount:BigDecimal,

               @Column(name = "currency_code")
               val currencyCode:String,

               @Column(name = "settled")
               val settled: Boolean,

               @Column(name = "transaction_id")
               val transactionId: String
)