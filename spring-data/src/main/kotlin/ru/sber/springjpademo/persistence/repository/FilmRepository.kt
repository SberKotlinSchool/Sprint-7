package ru.sber.springjpademo.persistence.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.springjpademo.persistence.entity.Film

@Repository
interface FilmRepository: CrudRepository<Film, Long>