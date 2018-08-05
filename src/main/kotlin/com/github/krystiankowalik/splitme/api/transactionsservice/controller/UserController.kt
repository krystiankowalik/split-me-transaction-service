package com.github.krystiankowalik.splitme.api.transactionsservice.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.security.Principal

@RequestMapping("/me")
interface UserController {

    @GetMapping
    fun getName(principal: Principal): ResponseEntity<String>

    @GetMapping("/roles")
    fun getRoles(principal: Principal): ResponseEntity<Any>
}