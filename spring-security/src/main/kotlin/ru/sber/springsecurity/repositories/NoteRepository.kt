package ru.sber.springsecurity.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.springsecurity.entities.Note

@Repository
interface NoteRepository: JpaRepository<Note, Long>