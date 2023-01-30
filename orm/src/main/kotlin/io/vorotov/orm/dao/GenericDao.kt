package io.vorotov.orm.dao

import io.vorotov.orm.dao.SessionTemplate.runWithResult

abstract class GenericDao<T, ID>(
    private val entityClass: Class<T>
) {

    fun save(entity: T) = runWithResult {
        it.save(entity)
        entity
    }

    fun findById(id: ID) = runWithResult { session ->
        session.find(entityClass, id)
    }

}
