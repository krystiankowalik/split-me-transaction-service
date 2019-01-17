package com.github.krystiankowalik.splitme.api.transactionsservice

import com.github.krystiankowalik.splitme.api.transactionsservice.io.TransactionRepository
import com.github.krystiankowalik.splitme.api.transactionsservice.model.transaction.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.orm.hibernate5.HibernateTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional("jpaTransactionManager")
class ScheduledTask {

    @Autowired lateinit var transactionManager: HibernateTransactionManager
    @Autowired lateinit var jpaTransactionManager: PlatformTransactionManager
    @Autowired lateinit var entityManagerFactory: LocalContainerEntityManagerFactoryBean
    @Autowired lateinit var transactionRepository: TransactionRepository


    @Scheduled(fixedRate = 300000)
    fun sth(){
        println("Hi, I'm working :)")
        val session = transactionManager.sessionFactory!!.openSession()
        val trx = session.beginTransaction()
        val transaction = session.get(Transaction::class.java, "1")
        println(transaction)

        trx.commit()
        session.close()

        val transaction2 = transactionRepository.getOne("1")
        println(transaction2)

    }
}