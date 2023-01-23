package ru.sber.springdata.repositories

import org.springframework.stereotype.Repository
import ru.sber.springdata.entities.Magazine
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

private const val GET_ALL_QUERY = "from Magazine"


interface CustomMagazinesRepository {

    fun getAllMagazine(): List<Magazine>
}

@Repository
class CustomMagazinesRepositoryImpl(
    @PersistenceContext
    private val entityManager: EntityManager
) : CustomMagazinesRepository {

    override fun getAllMagazine(): List<Magazine> =
        entityManager.createQuery(GET_ALL_QUERY, Magazine::class.java).resultList
}
