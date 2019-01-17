package com.github.krystiankowalik.splitme.api.transactionsservice.controller.impl

import com.github.krystiankowalik.splitme.api.transactionsservice.controller.DueController
import com.github.krystiankowalik.splitme.api.transactionsservice.model.due.Due
import com.github.krystiankowalik.splitme.api.transactionsservice.service.DueService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DueControllerImpl(val dueService: DueService) : DueController {
    override fun getDues(): ResponseEntity<List<Due>> {
        return ResponseEntity(dueService.getAll(), HttpStatus.OK)
    }

    override fun getDue(@PathVariable id: String): ResponseEntity<Due> {
        val foundDue = dueService.getByPublicId(id)
        return ResponseEntity(foundDue, HttpStatus.OK)
    }

    override fun toggleSettled(@PathVariable id: String, @RequestParam settled: Boolean): ResponseEntity<Due> {
        val found = dueService.getByPublicId(id)

        val new = Due(found.dueId, found.publicId, found.debtorId, found.creditorId, found.amount, found.currencyCode, settled, found.transactionId)
        dueService.save(new)
        return ResponseEntity(dueService.getByPublicId(new.publicId), HttpStatus.OK)
    }
}