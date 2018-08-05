package com.github.krystiankowalik.splitme.api.transactionsservice.model.user

data class Group(val id: String,
            val name: String,
            val users: List<User>?)