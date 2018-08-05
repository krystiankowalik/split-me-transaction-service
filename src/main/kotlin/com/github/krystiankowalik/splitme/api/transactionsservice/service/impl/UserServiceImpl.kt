package com.github.krystiankowalik.splitme.api.transactionsservice.service.impl

import com.github.krystiankowalik.splitme.api.transactionsservice.exception.UserNotFoundException
import com.github.krystiankowalik.splitme.api.transactionsservice.model.user.User
import com.github.krystiankowalik.splitme.api.transactionsservice.service.UserService
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.getForEntity
import org.springframework.web.client.getForObject
import java.util.*

@Service
class UserServiceImpl(val keycloakRestTemplate: KeycloakRestTemplate,
                      @Value("\${endpoint.user-service.users}") val userServiceUsersEndpoint: String) : UserService {

    override fun getUser(id: String): User? {
        val user = keycloakRestTemplate.getForObject<User>(url = "$userServiceUsersEndpoint/$id")
        user?.let { checkMissingIds(listOf(id), listOf(user)) }
        return user
    }

    override fun getUsers(): MutableList<Array<User>?>? {
        return Arrays.asList(keycloakRestTemplate.getForEntity<Array<User>>(url = "$userServiceUsersEndpoint/").body)
    }

    override fun getUsers(ids: List<String>): List<User>? {
        val joinedIds = ids.joinToString(",")
        val users = keycloakRestTemplate.getForEntity<Array<User>>(url = "$userServiceUsersEndpoint?ids=$joinedIds").body
        users?.let { checkMissingIds(ids, users.toList()) }

        return users?.toList()
    }

    private fun checkMissingIds(requestedUserIds: List<String>, foundUsers: List<User>) {
        foundUsers.let {
            val foundIds = foundUsers.map { it.id }
            val missingUsers = requestedUserIds.toMutableList()
            missingUsers.removeAll(foundIds)
            if (missingUsers.size > 0) {
                throw UserNotFoundException(message = "Could not find users with ids: ${missingUsers.joinToString(",")}")
            }
        }
    }

    override fun usersExist(ids:List<String>){
        getUsers(ids)
    }
}