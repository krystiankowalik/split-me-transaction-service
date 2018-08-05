package com.github.krystiankowalik.splitme.api.transactionsservice.model.user

data class User(val id: String,
           val username: String?,
           val firstName: String?,
           val lastName: String?,
           val email: String?)
//           val groups: List<Group>?)