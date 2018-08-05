package com.github.krystiankowalik.splitme.api.transactionsservice.service.impl

import com.github.krystiankowalik.splitme.api.transactionsservice.model.user.Group
import com.github.krystiankowalik.splitme.api.transactionsservice.service.GroupService
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.getForEntity
import org.springframework.web.client.getForObject
import java.util.*

@Service
class GroupServiceImpl(val keycloakRestTemplate: KeycloakRestTemplate,
                       @Value("\${endpoint.user-service.groups}") val userServiceGroupsEndpoint: String
) : GroupService {

    override fun getGroup(id: String): Group? {
        return keycloakRestTemplate.getForObject<Group>(url = "$userServiceGroupsEndpoint/$id")
    }

    override fun getGroups() = Arrays.asList(keycloakRestTemplate.getForEntity<Array<Group>>(url = "$userServiceGroupsEndpoint").body)
}