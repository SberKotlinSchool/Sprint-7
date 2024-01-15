package ru.sber.springjpademo.persistence.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.springjpademo.persistence.entity.Book

@Repository
interface BookRepository: CrudRepository<Book, Long>