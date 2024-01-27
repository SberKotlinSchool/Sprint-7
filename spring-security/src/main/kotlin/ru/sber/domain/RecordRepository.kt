package ru.sber.domain

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RecordRepository : CrudRepository<Record, Long> {

    @Query("SELECT record FROM Record record where record.id = :id and record.username = :username")
    fun findByIdAndUsername(@Param("id") id: Long, @Param("username") username: String): Record?

    @Query("SELECT record FROM Record record where record.username = :username")
    fun findAllByUsername(@Param("username") username: String): List<Record>

    @Query("SELECT record FROM Record record where record.name LIKE %:query%")
    fun findByNameContainingIgnoreCase(@Param("query") query: String): List<Record>

    @Query("SELECT record FROM Record record where record.username = :username AND record.name LIKE %:query%")
    fun findByNameContainingIgnoreCase(@Param("query") query: String, @Param("username") username: String): List<Record>
}