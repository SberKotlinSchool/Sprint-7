package ru.sber.dao

import org.hibernate.SessionFactory
import ru.sber.entities.Employee

class EmployeeDAO(private val sessionFactory: SessionFactory
) {
    fun save(employee: Employee) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(employee)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Employee? {
        val result: Employee?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Employee::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun find(email: String): Employee? {
        val result: Employee?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result =
                session.byNaturalId(Employee::class.java).using("email", email).loadOptional().orElse(null)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Employee> {
        val result: List<Employee>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Employee").list() as List<Employee>
            session.transaction.commit()
        }
        return result
    }
}
