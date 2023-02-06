package ru.sber.orm.db.config

import org.hibernate.cfg.Configuration
import ru.sber.orm.db.entity.Actor
import ru.sber.orm.db.entity.Film
import ru.sber.orm.db.entity.Producer

object Configuration {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Actor::class.java)
        .addAnnotatedClass(Film::class.java)
        .addAnnotatedClass(Producer::class.java)
        .buildSessionFactory()
}