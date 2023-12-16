package com.dokl57.ormproject.util

import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration

object HibernateUtils {
    val sessionFactory: SessionFactory

    init {
        try {
            val configuration = Configuration()
            configuration.configure()
            val serviceRegistry = StandardServiceRegistryBuilder()
                .applySettings(configuration.properties).build()
            sessionFactory = configuration.buildSessionFactory(serviceRegistry)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("There was an error building the factory")
        }
    }
}
