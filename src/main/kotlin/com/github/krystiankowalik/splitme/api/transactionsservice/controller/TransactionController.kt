package com.github.krystiankowalik.splitme.api.transactionsservice.controller

import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.Transaction
import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.TransactionRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RequestMapping("/transactions")
interface TransactionController {

    @GetMapping
    fun getTransactions(): ResponseEntity<List<Transaction>>

    @GetMapping("/{id}")
    fun getTransaction(@PathVariable id: String): ResponseEntity<Transaction>


    @PutMapping
    fun updateTransaction(@RequestBody requestedTransaction: TransactionRequest, principal: Principal): Transaction

    @DeleteMapping("/{id}")
    fun deleteTransaction(@PathVariable id: String): ResponseEntity<Void>

    @PostMapping
    fun saveTransaction(@RequestBody requestedTransaction: TransactionRequest, principal: Principal): Transaction

}