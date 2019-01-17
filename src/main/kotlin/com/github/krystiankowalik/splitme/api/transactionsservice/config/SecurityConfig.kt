package com.github.krystiankowalik.splitme.api.transactionsservice.config

import org.keycloak.adapters.KeycloakConfigResolver
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy

@KeycloakConfiguration
class SecurityConfig : KeycloakWebSecurityConfigurerAdapter() {


    /**
     * Registers the KeycloakAuthenticationProvider with the authentication manager.
     */
    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        val keycloakAuthenticationProvider = keycloakAuthenticationProvider()

        // adding proper authority mapper for prefixing role with "ROLE_"
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(SimpleAuthorityMapper())

        auth.authenticationProvider(keycloakAuthenticationProvider)
    }

    /**
     * Provide a session authentication strategy bean which should be of type
     * RegisterSessionAuthenticationStrategy for public or confidential applications
     * and NullAuthenticatedSessionStrategy for bearer-only applications.
     */
    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
    }

    /**
     * Use properties in application.properties instead of keycloak.json
     */
    @Bean
    fun KeycloakConfigResolver(): KeycloakConfigResolver {
        return KeycloakSpringBootConfigResolver()
    }

    /**
     * Secure appropriate endpoints
     */
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/dues*").hasRole("admin")
                .antMatchers("/dues/**").hasRole("admin")
                .antMatchers("/transactions*").hasRole("admin")
                .antMatchers("/transactions/**").hasRole("admin")
                .antMatchers("/me*").hasRole("user")
                .antMatchers("/me/**").hasRole("user")


    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**")
    }
}

