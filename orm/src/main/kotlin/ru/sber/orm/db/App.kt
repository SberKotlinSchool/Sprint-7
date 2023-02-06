package ru.sber.orm.db

import ru.sber.orm.db.config.Configuration
import ru.sber.orm.db.dao.ActorDao
import ru.sber.orm.db.entity.Actor
import ru.sber.orm.db.entity.Film
import ru.sber.orm.db.entity.Producer

fun main() {
    val actorDao = ActorDao(Configuration.sessionFactory)
    val actor = Actor(name = "Ivan", salary = 100_000)
    val producer = Producer(name = "Petr")
    val filmKotlin = Film(name = "Kotlin in Action", rating = 9.1f, budget = 999_999, producer = producer)
    val filmJava = Film(name = "Java 8", rating = 7.9f, budget = 121_144, producer = producer)
    actorDao.save(actor.apply { films = mutableListOf(filmKotlin , filmJava) })
    actorDao.delete(actor)
    val actorDb = actorDao.getById(1L)
}