package com.github.krystiankowalik.splitme.api.transactionsservice.controller.impl

import com.github.krystiankowalik.splitme.api.transactionsservice.controller.DueController
import com.github.krystiankowalik.splitme.api.transactionsservice.controller.UserDueController
import com.github.krystiankowalik.splitme.api.transactionsservice.exception.NotTransactionPartyException
import com.github.krystiankowalik.splitme.api.transactionsservice.model.due.Due
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class UserDueControllerImpl(val dueController: DueController) : UserDueController {
    /* override fun getDues(): ResponseEntity<List<Due>> {
         return ResponseEntity(dueService.getAll(), HttpStatus.OK)
     }

     override fun getDue(@PathVariable id: String): ResponseEntity<Due> {
         val foundDue = dueService.getByPublicId(id)
         return ResponseEntity(foundDue, HttpStatus.OK)
     }

     override fun toggleSettled(@PathVariable id: String, @RequestParam settled: Boolean): ResponseEntity<Due> {
         val found = dueService.getByPublicId(id)

         val new = Due(found.id, found.publicId, found.debtorId, found.creditorId, found.money, settled, found.transactionId)
         dueService.save(new)
         return ResponseEntity(dueService.getByPublicId(new.publicId), HttpStatus.OK)
     }*/

    override fun getDues(principal: Principal): ResponseEntity<List<Due>> {
        return ResponseEntity(dueController.getDues().body?.filter { it.debtorId == principal.name || it.creditorId == principal.name }, HttpStatus.OK)
    }

    override fun getDue(principal: Principal, @PathVariable id: String): ResponseEntity<Due> {
        val due = dueController.getDue(id).body!!
        return if (due.debtorId == principal.name || due.creditorId == principal.name) {
            ResponseEntity(due, HttpStatus.OK)
        } else {
            throw NotTransactionPartyException("User ${principal.name} is not entitled to access this detail")
        }
    }

    override fun toggleSettled(@PathVariable id: String, @RequestParam settled: Boolean, principal: Principal): ResponseEntity<Due> {
        val due = getDue(principal, id).body!!
        return dueController.toggleSettled(due.publicId, settled)
    }
}