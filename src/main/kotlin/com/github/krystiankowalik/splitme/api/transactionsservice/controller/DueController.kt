package com.github.krystiankowalik.splitme.api.transactionsservice.controller

import com.github.krystiankowalik.splitme.api.transactionsservice.model.due.Due
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/dues")
interface DueController {

    @GetMapping
    fun getDues(): ResponseEntity<List<Due>>

    @GetMapping("/{id}")
    fun getDue(id: String): ResponseEntity<Due>

    @PatchMapping("/{id}")
    fun toggleSettled(@PathVariable id: String, @RequestParam settled: Boolean): ResponseEntity<Due>
}