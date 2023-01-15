package dao

import entity.Person
import org.hibernate.SessionFactory

class PersonDao(
    override val sessionFactory: SessionFactory
) : BaseDao<Person> {
    fun findById(id: Long): Person? = findById(id, Person::class.java)
    fun findAll(): List<Person> = findAll(Person::class.java.simpleName)
}