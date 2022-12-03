package ru.sber.orm.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : CrudRepository<Book, Long> {
    fun findAllById(id: Long): List<Book>
}