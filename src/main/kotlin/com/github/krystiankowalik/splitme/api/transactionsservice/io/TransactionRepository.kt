package com.github.krystiankowalik.splitme.api.transactionsservice.io

import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.Transaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository : JpaRepository<Transaction, String> {

    fun findByPublicId(publicId: String): Transaction
    fun getByPublicId(id: String): Transaction


}