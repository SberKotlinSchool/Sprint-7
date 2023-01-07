package ru.sber.springdata.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.sber.springdata.entity.Model

@Repository
interface ModelRepository: CrudRepository<Model, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Model m SET m.name = :newName WHERE m.id = :idToUpdate")
    fun updateModelNameById(
        @Param("idToUpdate") id: Long,
        @Param("newName") newName: String
    )
}