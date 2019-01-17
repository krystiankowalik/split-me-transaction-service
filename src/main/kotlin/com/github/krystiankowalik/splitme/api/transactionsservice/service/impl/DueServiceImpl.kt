package com.github.krystiankowalik.splitme.api.transactionsservice.service.impl

import com.github.krystiankowalik.splitme.api.transactionsservice.exception.DueNotFoundException
import com.github.krystiankowalik.splitme.api.transactionsservice.io.DueRepository
import com.github.krystiankowalik.splitme.api.transactionsservice.io.TransactionRepository
import com.github.krystiankowalik.splitme.api.transactionsservice.model.due.Due
import com.github.krystiankowalik.splitme.api.transactionsservice.service.DueService
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service

@Service

class DueServiceImpl(val transactionRepository: TransactionRepository,
                     val dueRepository: DueRepository) : DueService {


    /*   override fun getAllByDebtorId(id: String): List<Due> {
           return transactionRepository
                   .findDuesByDues_DebtorId("fcfbeef8-eb95-433f-a8e4-26f3ea5d315d")

       }*/
    override fun getAllByDebtorId(id: String): List<Due> {
        return dueRepository.findAllByDebtorId(id)
    }

    override fun getAllByCreditorId(id: String): List<Due> {
        return dueRepository.findAllByCreditorId(id)
    }

    override fun save(due: Due) {
        dueRepository.save(due)
    }

    override fun saveAll(dues: List<Due>) {
        dueRepository.saveAll(dues)
    }

    override fun deleteByTransactionId(id: String) {
        dueRepository.deleteAllByTransactionId(id)
    }

    override fun getByTransactionId(id: String): List<Due> {
        return dueRepository.findAllByTransactionId(id)
    }

    override fun getAll() = dueRepository.findAll()

    override fun getByPublicId(id: String): Due {
        return try{
            dueRepository.findByPublicId(id)
        }catch (e: EmptyResultDataAccessException){
            throw DueNotFoundException(message = "Could not find due with id: $id", cause = e)
        }
    }
}