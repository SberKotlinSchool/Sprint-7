package ru.sber.springdata.persistence.repository

import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import ru.sber.springdata.persistence.entity.BookEntity
import ru.sber.springdata.persistence.entity.LibraryEntity

interface BookRepository : CrudRepository<BookEntity, Long> {

  fun findAllByLibrary(libraryEntity: LibraryEntity, pageable: Pageable): Page<BookEntity>

  @Modifying
  @Transactional
  @Query("delete from books where library_id=:id", nativeQuery = true)
  fun deleteAllByLibraryId(@Param("id") id: Long)
}