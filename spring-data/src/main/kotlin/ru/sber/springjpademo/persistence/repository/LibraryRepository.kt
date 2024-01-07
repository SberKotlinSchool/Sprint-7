package ru.sber.springjpademo.persistence.repository

import org.springframework.data.repository.CrudRepository
import ru.sber.springjpademo.persistence.entity.LibraryEntity

interface LibraryRepository: CrudRepository<LibraryEntity, Long> {

    fun findByName(name: String): LibraryEntity?
}