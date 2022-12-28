package ru.sber.orm

import org.hibernate.cfg.Configuration
import ru.sber.orm.dao.ModelDAO
import ru.sber.orm.entities.Manufacturer
import ru.sber.orm.entities.Model

class OrmApplication

fun main() {

    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Manufacturer::class.java)
        .addAnnotatedClass(Model::class.java)
        .buildSessionFactory()

    sessionFactory.use {
        val modelDAO = ModelDAO(sessionFactory)


    }
}
