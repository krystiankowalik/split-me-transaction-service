package com.github.krystiankowalik.splitme.api.transactionsservice.service

import com.github.krystiankowalik.splitme.api.transactionsservice.model.user.Group

interface GroupService {
    fun getGroup(id: String): Group?
    fun getGroups(): MutableList<Array<Group>?>?
}