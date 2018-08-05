package com.github.krystiankowalik.splitme.api.transactionsservice.service

import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.Transaction

interface TransactionService {
    fun save(transaction: Transaction)
    fun getAll(): List<Transaction>
    fun getById(id: String): Transaction
    fun delete(id: String)
    fun deleteByPublicId(publicId: String)
    fun exists(id: String): Boolean
    fun getByPublicId(publicId: String): Transaction
    fun isUserPartyToTransaction(userId: String, transactionId: String): Boolean
    fun ensureUserIsPartyToTransaction(userId: String, transactionId: String)
    fun getTransactionParties(publicId: String): List<String>
}