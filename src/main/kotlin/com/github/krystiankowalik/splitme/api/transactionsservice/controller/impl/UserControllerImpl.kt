package com.github.krystiankowalik.splitme.api.transactionsservice.controller.impl

import com.github.krystiankowalik.splitme.api.transactionsservice.controller.UserController
import com.github.krystiankowalik.splitme.api.transactionsservice.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class UserControllerImpl(val userService: UserService) : UserController {


    override fun getName(principal: Principal): ResponseEntity<String> {
        return ResponseEntity(principal.name, HttpStatus.OK)
    }

    override fun getRoles(principal: Principal): ResponseEntity<Any> {

        return ResponseEntity(userService.getUser(principal.name),HttpStatus.OK)
    }
}