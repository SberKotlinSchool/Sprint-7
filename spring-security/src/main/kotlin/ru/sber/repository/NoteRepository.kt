package ru.sber.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.model.Note

@Repository
interface NoteRepository : CrudRepository<Note, Long> {

    fun getNoteById(id: Long): Note

    fun searchNoteByName(s: String): List<Note>

    fun searchNoteByAddress(s: String): List<Note>

    fun searchNoteByPhone(s: String): List<Note>
}