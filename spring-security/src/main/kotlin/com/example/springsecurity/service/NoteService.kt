package com.example.springsecurity.service

import com.example.springsecurity.entity.Note
import com.example.springsecurity.repository.NoteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NoteService @Autowired constructor(
    private val noteRepository: NoteRepository,
    private val userService: UserService
) {

    fun editNote(id: Long, text: String): List<Note> {
        noteRepository.updateNoteById(id, text)
        return noteRepository.getNotesByUser(userService.getUserFromContext())

//        val storedNote = noteRepository.findById(id).orElse(null)
//
//        if (storedNote != null) {
//            noteRepository.deleteById(id)
//            storedNote.text = text
//            noteRepository.save(storedNote)
//            return noteRepository.getNotesByUser(storedNote.author)
//        }
//
//        return emptyList()
    }

    fun saveNote(note: Note) {
        noteRepository.save(note)
    }

    fun findAllNotesByAuthor(name: String): List<Note> {
        return noteRepository.getNotesByUser(name)
    }

    fun deleteNoteById(id: Long) {
        noteRepository.deleteById(id)
    }
}