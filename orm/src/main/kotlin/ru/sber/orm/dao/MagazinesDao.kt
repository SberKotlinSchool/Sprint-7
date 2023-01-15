package ru.sber.orm.dao

import org.hibernate.SessionFactory
import ru.sber.orm.entities.Magazine

private const val GET_ALL_QUERY = "from Magazine"
private const val MAGAZINE_NAME_COLUMN = "name"

class MagazinesDao(private val sessionFactory: SessionFactory) {

    fun save(magazine: Magazine) {
        sessionFactory.openSession().use { session ->
            session.run {
                runCatching {
                    beginTransaction()
                    session.save(magazine)
                    transaction.commit()
                }.onFailure {
                    println(it.message)
                    transaction.rollback()
                }
            }
        }
    }

    fun get(id: Long): Magazine? {
        val result: Magazine?
        sessionFactory.openSession().use { session ->
            session.run {
                result = runCatching {
                    beginTransaction()
                    val resultFromDatabase = session.get(Magazine::class.java, id)
                    transaction.commit()

                    resultFromDatabase
                }.onFailure {
                    println(it.message)
                    transaction.rollback()
                }.getOrNull()
            }
        }

        return result
    }

    fun findByName(name: String): Magazine? {
        val result: Magazine?
        sessionFactory.openSession().use { session ->
            session.run {
                result = runCatching {
                    beginTransaction()
                    val resultFromDatabase = session.byNaturalId(Magazine::class.java)
                        .using(MAGAZINE_NAME_COLUMN, name)
                        .loadOptional()
                        .orElse(null)
                    transaction.commit()

                    resultFromDatabase
                }.onFailure {
                    println(it.message)
                    transaction.rollback()
                }.getOrNull()
            }
        }
        return result
    }

    fun getAll(): List<Magazine> {
        val result: List<Magazine>
        sessionFactory.openSession().use { session ->
            session.run {
                result = runCatching {
                    beginTransaction()
                    val resultFromDatabase = session.createQuery(GET_ALL_QUERY, Magazine::class.java).list()
                    transaction.commit()

                    resultFromDatabase
                }.onFailure {
                    println(it.message)
                    transaction.rollback()
                }.getOrDefault(emptyList())
            }
        }

        return result
    }
}
