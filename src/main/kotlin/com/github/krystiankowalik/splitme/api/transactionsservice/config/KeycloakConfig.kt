package com.github.krystiankowalik.splitme.api.transactionsservice.config

import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope


@Configuration
class KeycloakConfig {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun keycloakRestTemplate(keycloakClientRequestFactory: KeycloakClientRequestFactory): KeycloakRestTemplate {

        return KeycloakRestTemplate(keycloakClientRequestFactory)
    }
}