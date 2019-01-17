package com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.split

import com.fasterxml.jackson.annotation.JsonIgnore
import com.github.krystiankowalik.splitme.api.transactionsservice.model.due.Due
import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.Transaction
import com.github.krystiankowalik.splitme.api.transactionsservice.util.generateRandomId
import java.io.Serializable
import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "split")
data class Split(@Id
                 @Column(name = "split_id")
                 val split_id: String="",
                 @OneToMany
                 @JoinColumn(name = "split_id", referencedColumnName = "split_id")
                 val userAmountsList: List<UserAmounts> = arrayListOf(),
                 @Column(name = "currency")
                 val currency: String="") : Serializable {

    @Transient
    private fun getUserAmountsMap() = userAmountsList.map { it.userId to it }.toMap()

    @Transient
    fun getTotalAmount() = getUserAmountsMap().map { it.value.exchanged }.fold(BigDecimal.ZERO, BigDecimal::add).roundWithContext()



    @JsonIgnore
    @Transient
    private val totalShare = getUserAmountsMap().map { it.value.share }.fold(BigDecimal.ZERO, BigDecimal::add).roundWithContext()

    @JsonIgnore
    @Transient
    private val isIncome = getTotalAmount()> BigDecimal.ZERO

    @JsonIgnore
    @Transient
    val transactionType = if (isIncome) Transaction.Type.INCOME else Transaction.Type.EXPENSE

    @JsonIgnore
    private fun UserAmounts.getUserShareRatio(): BigDecimal {
        val ratio = userAmountsList[userAmountsList.indexOf(this)].share.divideWithScale(totalShare)
        return ratio

    }

    @JsonIgnore
    private fun BigDecimal.adjustSign() = this.abs().multiply(transactionType.factor)

    @JsonIgnore
    private fun UserAmounts.getUserShareAsAmount(): BigDecimal =
            (this.getUserShareRatio().multiply(getTotalAmount())).adjustSign()

    @JsonIgnore
    private fun UserAmounts.getUserDueTotalAmount(): BigDecimal {
        val dueAmount = this.exchanged - this.getUserShareAsAmount()
        return dueAmount
    }

    @JsonIgnore
    private fun getTotalOverpaid(): BigDecimal =
            userAmountsList
                    .filter { it.getUserDueTotalAmount() > BigDecimal.ZERO }
                    .map { it.getUserDueTotalAmount() }
                    .fold(BigDecimal.ZERO, BigDecimal::add)
                    .roundWithContext()

    @JsonIgnore
    @Transient
    val creditors = userAmountsList.filter { it.getUserDueTotalAmount() < BigDecimal.ZERO }
    @JsonIgnore
    @Transient
    val debtors = userAmountsList.filter { it.getUserDueTotalAmount() >= BigDecimal.ZERO }

    @JsonIgnore
    fun calculateDues(transactionId: String): List<Due> {
        val dues = mutableListOf<Due>()
        debtors.forEach { debtor ->
            creditors.forEach { creditor ->
                dues.add(Due(dueId = generateRandomId(),
                        publicId = generateRandomId(),
                        debtorId = debtor.userId,
                        creditorId = creditor.userId,
                        amount = debtor
                                .getUserDueTotalAmount()
                                .multiply(creditor
                                        .getUserDueTotalAmount()
                                        .divideWithScale(getTotalOverpaid()).abs()).roundWithContext(),
                        currencyCode = this.currency,
                        settled = false,
                        transactionId = transactionId))
            }
        }
        creditors.forEach { debtor ->
            creditors.forEach { creditor ->
                if (debtor != creditor) {
                    dues.add(Due(dueId = generateRandomId(),
                            publicId = generateRandomId(),
                            debtorId = debtor.userId,
                            creditorId = creditor.userId,
                            amount = debtor
                                    .getUserDueTotalAmount()
                                    .multiply(creditor
                                            .getUserDueTotalAmount()
                                            .divideWithScale(getTotalOverpaid()).abs()).roundWithContext(),
                            currencyCode = currency,
                            settled = false,
                            transactionId = transactionId))
                }
            }
        }
        return dues.filter { it.amount > BigDecimal.ZERO && it.amount.abs() > BigDecimal("0.001") }
    }

    private fun BigDecimal.divideWithScale(other: BigDecimal): BigDecimal = this.divide(other, 30, java.math.RoundingMode.FLOOR)

    private fun BigDecimal.roundWithContext() = this.round(java.math.MathContext(4, java.math.RoundingMode.FLOOR))


}

