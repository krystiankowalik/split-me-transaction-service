package com.github.krystiankowalik.splitme.api.transactionsservice.service

import com.github.krystiankowalik.splitme.api.transactionsservice.model.user.User

interface UserService {

    fun getUsers(): MutableList<Array<User>?>?

    fun getUser(id: String): User?
    fun getUsers(ids: List<String>): List<User>?
    fun usersExist(ids: List<String>)
}