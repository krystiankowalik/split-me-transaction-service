package com.github.krystiankowalik.splitme.api.transactionsservice.service

import com.github.krystiankowalik.splitme.api.transactionsservice.model.due.Due

interface DueService {

    fun getAllByDebtorId(id:String): List<Due>

    fun save(due: Due)
    fun saveAll(dues: List<Due>)
    fun deleteByTransactionId(id: String)
    fun getAll(): MutableList<Due>
    fun getByPublicId(id: String): Due
    fun getByTransactionId(id: String): List<Due>
    fun getAllByCreditorId(id: String): List<Due>
}