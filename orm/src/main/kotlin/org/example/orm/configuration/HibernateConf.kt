package org.example.orm.configuration

import org.example.orm.model.Address
import org.example.orm.model.Passport
import org.example.orm.model.Person
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
@EnableTransactionManagement
@ComponentScan("org.example.orm.dao")
open class HibernateConf {
    @Bean
    open fun sessionFactory() = org.hibernate.cfg.Configuration().configure()
            .addAnnotatedClass(Person::class.java)
            .addAnnotatedClass(Passport::class.java)
            .addAnnotatedClass(Address::class.java)
            .buildSessionFactory()
}