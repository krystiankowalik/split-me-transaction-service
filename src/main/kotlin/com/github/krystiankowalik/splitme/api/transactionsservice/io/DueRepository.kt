package com.github.krystiankowalik.splitme.api.transactionsservice.io

import com.github.krystiankowalik.splitme.api.transactionsservice.model.due.Due
import org.springframework.data.mongodb.repository.MongoRepository

interface DueRepository : MongoRepository<Due, String> {

    fun findAllByDebtorId(id:String):List<Due>
    fun findAllByCreditorId(id:String):List<Due>
    fun findAllByTransactionId(id:String):List<Due>
    fun deleteAllByTransactionId(id:String):List<Due>
    fun findByPublicId(id: String): Due


}