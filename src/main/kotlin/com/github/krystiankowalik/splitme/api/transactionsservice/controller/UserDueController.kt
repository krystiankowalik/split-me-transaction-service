package com.github.krystiankowalik.splitme.api.transactionsservice.controller

import com.github.krystiankowalik.splitme.api.transactionsservice.model.due.Due
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RequestMapping("/me/dues")
interface UserDueController {


    @GetMapping("/{id}")
    fun getDue(principal: Principal, id: String): ResponseEntity<Due>

    @PatchMapping("/{id}")
    fun toggleSettled(@PathVariable id: String, @RequestParam settled: Boolean, principal: Principal): ResponseEntity<Due>

    @GetMapping
    fun getDues(principal: Principal): ResponseEntity<List<Due>>
}