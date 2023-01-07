package ru.sber.springsecurity.controllers.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.sber.springsecurity.entities.Note
import ru.sber.springsecurity.services.NoteService

@RestController
@RequestMapping("/api/message")
class MessageRestController @Autowired constructor(val noteService: NoteService) {

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    fun getAllMessages(): List<Note> {
        return noteService.findAllNotes()
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping
    fun getMessage(@RequestParam(value = "id", defaultValue = "0") id: Int): Note? {
        return noteService.findNoteById(id.toLong())
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/delete")
    fun deleteMessage(@RequestParam(value = "id", defaultValue = "0") id: Int): Note? {
        val noteToDelete = noteService.findNoteById(id.toLong())
        noteService.deleteNoteById(id.toLong())
        return noteToDelete
    }
}