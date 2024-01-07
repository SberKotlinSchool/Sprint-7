package ru.sber.springjpademo.persistence.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import ru.sber.springjpademo.persistence.entity.BookEntity
import ru.sber.springjpademo.persistence.entity.LibraryEntity

interface BookRepository : PagingAndSortingRepository<BookEntity, Long> {

    fun findAllByLibrary(libraryEntity: LibraryEntity, pageable: Pageable): Page<BookEntity>

    @Modifying
    @Transactional
    @Query("delete from books where library_id=:id", nativeQuery = true)
    fun deleteAllByLibraryId(@Param("id") id: Long)
}