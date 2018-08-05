package com.github.krystiankowalik.splitme.api.transactionsservice.io

import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.Transaction
import org.springframework.data.mongodb.repository.MongoRepository

interface TransactionRepository : MongoRepository<Transaction, String> {

    fun findByPublicId(publicId: String): Transaction
    fun getByPublicId(id: String): Transaction


}