package com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.split

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "split_detail")
data class UserAmounts(
        @Id
        @Column(name = "user_id")
        val userId: String="",
        @Column(name = "split_id")
        val splitId: String="",
        @Column(name = "exchanged_amount")
        val exchanged: BigDecimal= BigDecimal.ZERO,
        @Column(name = "user_share")
        val share: BigDecimal= BigDecimal.ZERO
)