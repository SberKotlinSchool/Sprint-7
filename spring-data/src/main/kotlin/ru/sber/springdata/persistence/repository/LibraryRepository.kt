package ru.sber.springdata.persistence.repository

import org.springframework.data.repository.CrudRepository
import ru.sber.springdata.persistence.entity.LibraryEntity

interface LibraryRepository: CrudRepository<LibraryEntity, Long> {

  fun findByName(name: String): LibraryEntity?
}