package ru.sber.orm.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.orm.entities.Author
import ru.sber.orm.entities.Book

@Repository
interface AuthorRepository :JpaRepository <Author, Long>  {
}