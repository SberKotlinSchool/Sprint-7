package ru.sber.orm.dao

import org.hibernate.SessionFactory
import ru.sber.orm.entities.Model

class ModelDAO (private val session: SessionFactory) {

    fun save(model: Model) {
        session.openSession().use {
            it.beginTransaction()
            it.saveOrUpdate("model", model)
            it.transaction.commit()
        }
    }

    fun findById(id: Long): Model? {
        var result: Model?

        session.openSession().use {
            it.beginTransaction()
            result = it.get(Model::class.java, id)
            it.transaction.commit()
        }
        return result
    }

    fun findByName(name: String): Model? {
        var result: Model?

        session.openSession().use {
            it.beginTransaction()
            result = it.byNaturalId(Model::class.java).using("name", name).loadOptional().orElse(null)
            it.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Model> {
        var result: List<Model>

        session.openSession().use {
            it.beginTransaction()
            result = it.createQuery("from Model", Model::class.java).list()
            it.transaction.commit()
        }
        return result
    }
}