package com.github.krystiankowalik.splitme.api.transactionsservice.controller.impl

import com.github.krystiankowalik.splitme.api.transactionsservice.controller.TransactionController
import com.github.krystiankowalik.splitme.api.transactionsservice.controller.UserTransactionController
import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.Transaction
import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.TransactionRequest
import com.github.krystiankowalik.splitme.api.transactionsservice.service.DueService
import com.github.krystiankowalik.splitme.api.transactionsservice.service.GroupService
import com.github.krystiankowalik.splitme.api.transactionsservice.service.TransactionService
import com.github.krystiankowalik.splitme.api.transactionsservice.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class UserTransactionControllerImpl(val transactionService: TransactionService,
                                    val dueService: DueService,
                                    val userService: UserService,
                                    val groupService: GroupService,
                                    val transactionController: TransactionController) : UserTransactionController {


    override fun saveTransaction(@RequestBody requestedTransaction: TransactionRequest, principal: Principal): Transaction {
        return transactionController.saveTransaction(requestedTransaction,principal)
    }

    override fun updateTransaction(@RequestBody requestedTransaction: TransactionRequest, principal: Principal): Transaction {
        transactionService.ensureUserIsPartyToTransaction(principal.name, requestedTransaction.publicId)
        return transactionController.updateTransaction(requestedTransaction,principal)
    }


    override fun getTransactions(principal: Principal): ResponseEntity<List<Transaction>> {
        val allTransactions = transactionService.getAll()
        val usersTransactions = allTransactions.filter { transactionService.isUserPartyToTransaction(principal.name, it.publicId) }
        return ResponseEntity(usersTransactions, HttpStatus.OK)
    }

    override fun getTransaction(@PathVariable id: String, principal: Principal): ResponseEntity<Transaction> {
        transactionService.ensureUserIsPartyToTransaction(principal.name, id)
        return ResponseEntity(transactionService.getByPublicId(id), HttpStatus.OK)
    }

    override fun deleteTransaction(@PathVariable id: String, principal: Principal): ResponseEntity<Void> {
        transactionService.ensureUserIsPartyToTransaction(principal.name, id)
        dueService.deleteByTransactionId(id)
        transactionService.deleteByPublicId(id)
        return ResponseEntity.ok().build<Void>()
    }


}