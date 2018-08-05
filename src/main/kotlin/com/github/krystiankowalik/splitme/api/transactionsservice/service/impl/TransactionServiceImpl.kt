package com.github.krystiankowalik.splitme.api.transactionsservice.service.impl

import com.github.krystiankowalik.splitme.api.transactionsservice.exception.NotTransactionPartyException
import com.github.krystiankowalik.splitme.api.transactionsservice.exception.TransactionConsistencyException
import com.github.krystiankowalik.splitme.api.transactionsservice.exception.TransactionNotFoundException
import com.github.krystiankowalik.splitme.api.transactionsservice.io.TransactionRepository
import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.Transaction
import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.split.Split
import com.github.krystiankowalik.splitme.api.transactionsservice.service.TransactionService
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionServiceImpl(val transactionRepository: TransactionRepository) : TransactionService {


    override fun save(transaction: Transaction) {
        checkUserExchangedAmountsConsistency(transaction.split)
        checkUserSharesConsistency(transaction.split)
        transactionRepository.save(transaction)
    }

    private fun checkUserExchangedAmountsConsistency(split: Split) {
        val amounts = split.userAmountsList.map { it.exchanged }
        val notAllAmountsArePositive = !amounts.all { it >= BigDecimal.ZERO }
        val notAllAmountsAreNegative = !amounts.all { it <= BigDecimal.ZERO }
        if (notAllAmountsArePositive && notAllAmountsAreNegative) {
            throw TransactionConsistencyException("Transaction amounts paid by individual users are inconsistent. " +
                    "All amounts paid/received by users in the same transaction must be either positive or negative.")
        }
    }
    private fun checkUserSharesConsistency(split: Split) {
        val shares = split.userAmountsList.map { it.share }
        val notAllSharesArePositive = !shares.all { it >= BigDecimal.ZERO }
        val notAllSharesAreNegative = !shares.all { it <= BigDecimal.ZERO }
        if (notAllSharesArePositive && notAllSharesAreNegative) {
            throw TransactionConsistencyException("Transaction shares specified for individual users are inconsistent. " +
                    "All users' shares in the same transaction must be either positive or negative.")
        }
    }


    override fun isUserPartyToTransaction(userId: String, transactionId: String): Boolean {
        val foundTransactionUserIds = getTransactionParties(transactionId)
        return foundTransactionUserIds.contains(userId)
    }

    override fun ensureUserIsPartyToTransaction(userId: String, transactionId: String) {
        if (!isUserPartyToTransaction(userId, transactionId)) {
            throw NotTransactionPartyException("User $userId is not party to transaction $transactionId")
        }
    }

    override fun getTransactionParties(publicId: String): List<String> {
        return getByPublicId(publicId).split.userAmountsList.map { it.userId }
    }

    override fun getAll(): List<Transaction> = transactionRepository.findAll()

    override fun getById(id: String): Transaction = transactionRepository.findById(id).get()

    override fun getByPublicId(publicId: String): Transaction {
        return try {
            transactionRepository.getByPublicId(publicId)
        } catch (e: EmptyResultDataAccessException) {
            throw TransactionNotFoundException(message = "Could not find transaction with id: $publicId", cause = e)

        }
    }

    override fun delete(id: String) {
        transactionRepository.deleteById(id)
    }

    override fun deleteByPublicId(publicId: String) {
        val transactionToDelete = transactionRepository.findByPublicId(publicId)
        delete(transactionToDelete.id)

    }

    override fun exists(id: String) = transactionRepository.existsById(id)

}