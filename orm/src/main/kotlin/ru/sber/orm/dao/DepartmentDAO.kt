package ru.sber.orm.dao

import org.hibernate.SessionFactory
import ru.sber.orm.entities.Department

class DepartmentDAO(private val sessionFactory: SessionFactory
) {
    fun save(department: Department) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(department)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Department? {
        val result: Department?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Department::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Department> {
        val result: List<Department>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Department").list() as List<Department>
            session.transaction.commit()
        }
        return result
    }
}