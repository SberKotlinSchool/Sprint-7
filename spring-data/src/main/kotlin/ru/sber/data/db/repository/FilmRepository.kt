package ru.sber.data.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.data.db.entity.Film

interface FilmRepository: JpaRepository<Film, Long>