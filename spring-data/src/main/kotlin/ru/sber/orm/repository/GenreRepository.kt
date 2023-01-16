package ru.sber.orm.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.orm.entities.Book
import ru.sber.orm.entities.Genre

@Repository
interface GenreRepository :JpaRepository <Genre, Long>  {
}