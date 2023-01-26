package ru.sber.astafex.springdata.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.astafex.springdata.entity.Genre
import ru.sber.astafex.springdata.entity.Movie

@Repository
interface MovieRepository : CrudRepository<Movie, Long> {
    fun findByGenre(genre: Genre): List<Movie>
}