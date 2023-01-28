package dao

import entity.Passport
import org.hibernate.SessionFactory

class PassportDao(
    override val sessionFactory: SessionFactory
) : BaseDao<Passport> {
    fun findById(id: Long): Passport? = findById(id, Passport::class.java)
    fun findAll(): List<Passport> = findAll(Passport::class.java.simpleName)
}