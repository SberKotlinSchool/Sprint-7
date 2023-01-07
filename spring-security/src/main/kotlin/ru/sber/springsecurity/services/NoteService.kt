package ru.sber.springsecurity.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.sber.springsecurity.entities.Note
import ru.sber.springsecurity.repositories.NoteRepository
import ru.sber.springsecurity.utils.RequestUtils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class NoteService @Autowired constructor(val noteRepository: NoteRepository) {
    fun findAllNotes(): List<Note> = noteRepository.findAll()

    fun findNoteById(id: Long): Note? = noteRepository.findById(id).orElse(null)

    fun saveNote(note: Note) {
        note.postDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(RequestUtils.DAY_MONTH_HMS))
        noteRepository.save(note)
    }

    fun deleteNoteById(id: Long) = noteRepository.deleteById(id)

    fun editNoteById(id: Long, note: Note) {
        val storedNote = noteRepository.findById(id).orElse(null)
        if (storedNote != null) {
            storedNote.content = note.content
            storedNote.author = note.author
            storedNote.postDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(RequestUtils.DAY_MONTH_HMS))

            noteRepository.save(storedNote)
        }
    }
}