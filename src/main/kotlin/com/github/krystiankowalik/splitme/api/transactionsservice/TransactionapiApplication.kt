package com.github.krystiankowalik.splitme.api.transactionsservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
@EnableTransactionManagement
//@EnableJpaRepositories("com.github.krystiankowalik.splitme.api.transactionsservice.io",
//        entityManagerFactoryRef = "entityManagerFactory",
//        transactionManagerRef = "jpaTransactionManager")
class TransactionapiApplication

fun main(args: Array<String>) {
    runApplication<TransactionapiApplication>(*args)
}
