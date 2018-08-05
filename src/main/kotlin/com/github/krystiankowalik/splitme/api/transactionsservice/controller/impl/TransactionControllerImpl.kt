package com.github.krystiankowalik.splitme.api.transactionsservice.controller.impl

import com.github.krystiankowalik.splitme.api.transactionsservice.controller.TransactionController
import com.github.krystiankowalik.splitme.api.transactionsservice.exception.NotTransactionPartyException
import com.github.krystiankowalik.splitme.api.transactionsservice.exception.TransactionModificationException
import com.github.krystiankowalik.splitme.api.transactionsservice.model.money.Money
import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.Transaction
import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.TransactionRequest
import com.github.krystiankowalik.splitme.api.transactionsservice.service.DueService
import com.github.krystiankowalik.splitme.api.transactionsservice.service.GroupService
import com.github.krystiankowalik.splitme.api.transactionsservice.service.TransactionService
import com.github.krystiankowalik.splitme.api.transactionsservice.service.UserService
import com.github.krystiankowalik.splitme.api.transactionsservice.util.generateRandomId
import com.github.krystiankowalik.splitme.api.transactionsservice.util.getCurrentTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class TransactionControllerImpl(val transactionService: TransactionService,
                                val dueService: DueService,
                                val userService: UserService,
                                val groupService: GroupService) : TransactionController {


    override fun saveTransaction(@RequestBody requestedTransaction: TransactionRequest, principal: Principal): Transaction {

        userService.usersExist(requestedTransaction.split.userAmountsList.map { it.userId })
        if (!requestedTransaction.split.userAmountsList.map { it.userId }.contains(principal.name)) {
            throw NotTransactionPartyException("The user adding a transaction must be party to it: ${principal.name}")
        }
        val transactionToSave = Transaction(
                id = generateRandomId(),
                publicId = generateRandomId(),
                date = requestedTransaction.date,
                description = requestedTransaction.description,
                money = Money(
                        amount = requestedTransaction.split.totalAmount,
                        currency = requestedTransaction.split.splitCurrency
                ),
                type = requestedTransaction.split.transactionType,
                split = requestedTransaction.split,
                added = getCurrentTime(),
                addedBy = principal.name,
                lastModified = getCurrentTime(),
                lastModifiedBy = principal.name
        )

        transactionService.save(transactionToSave)
        dueService.saveAll(transactionToSave.split.calculateDues(transactionToSave.publicId))

        val savedTransaction = transactionService.getByPublicId(transactionToSave.publicId)
        return savedTransaction
    }

    override fun updateTransaction(@RequestBody requestedTransaction: TransactionRequest, principal: Principal): Transaction {
        val oldTransaction = transactionService.getByPublicId(requestedTransaction.publicId)
        val transactionDues = dueService.getByTransactionId(oldTransaction.publicId)
        if (transactionDues.map { it.settled }.contains(true)) {
            throw TransactionModificationException("Transaction is partially settled - cannot modify")
        }
        val transactionToSave = Transaction(
                id = oldTransaction.id,
                publicId = oldTransaction.publicId,
                date = requestedTransaction.date,
                description = requestedTransaction.description,
                money = Money(
                        amount = requestedTransaction.split.totalAmount,
                        currency = requestedTransaction.split.splitCurrency
                ),
                type = requestedTransaction.split.transactionType,
                split = requestedTransaction.split,
                added = oldTransaction.added,
                addedBy = oldTransaction.addedBy,
                lastModified = getCurrentTime(),
                lastModifiedBy = principal.name
        )
        dueService.deleteByTransactionId(oldTransaction.publicId)
        dueService.saveAll(transactionToSave.split.calculateDues(oldTransaction.publicId))

        transactionService.save(transactionToSave)
        return transactionService.getByPublicId(transactionToSave.publicId)

    }

    override fun getTransactions() = ResponseEntity(transactionService.getAll(), HttpStatus.OK)

    override fun getTransaction(@PathVariable id: String) = ResponseEntity(transactionService.getByPublicId(id), HttpStatus.OK)

    override fun deleteTransaction(@PathVariable id: String): ResponseEntity<Void> {
        dueService.deleteByTransactionId(id)
        transactionService.deleteByPublicId(id)
        return ResponseEntity.ok().build<Void>()
    }

    @GetMapping("/test2")
    fun getUsers(): String {
        //return dueService.getAllByDebtorId("0b1e8311-e16b-4c5e-87e3-7eca38a0727b")
        return "dupa"
    }


}