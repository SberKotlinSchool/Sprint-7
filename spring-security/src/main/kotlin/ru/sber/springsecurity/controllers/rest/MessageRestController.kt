package ru.sber.springsecurity.controllers.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.sber.springsecurity.entities.Note
import ru.sber.springsecurity.entities.Note.Companion.deleteNote
import ru.sber.springsecurity.utils.RequestUtils.Companion.notes

@RestController
@RequestMapping("/api/message")
class MessageRestController {

    @GetMapping("/all")
    fun getAllMessages(): List<Note> {
        return notes
    }

    @GetMapping
    fun getMessage(@RequestParam(value = "id", defaultValue = "0") id: Int): Note? {
        if (notes.size > id) {
            return notes[id]
        }
        return null
    }

    @GetMapping("/delete")
    fun deleteMessage(@RequestParam(value = "id", defaultValue = "0") id: Int): Note? {
        if (notes.size > id) {
            val noteToDelete = notes[id]
            deleteNote(id)
            return noteToDelete
        }
        return null
    }

    @GetMapping("/author")
    fun getMessagesByAuthor(@RequestParam(value = "name", defaultValue = "0") name: String): List<Note> {
        return notes.filter { it.author == name }.toList()
    }

}