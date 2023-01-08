package com.example.springsecurity.controllers.rest

import com.example.springsecurity.entity.Note
import com.example.springsecurity.repository.NoteRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/notes")
class NoteRestController @Autowired constructor(private val noteRepository: NoteRepository) {

    @GetMapping
    fun getNotes(@RequestParam(value = "login") login: String) : List<Note>{
        return noteRepository.getNotesByUser(login)
    }

    @PostMapping("/{login}/delete/{id}")
    fun deleteNote(@PathVariable("id") id: String, @PathVariable("login") login: String) : List<Note>{
        noteRepository.deleteById(id.toLong())
        return noteRepository.getNotesByUser(login)
    }

    @PostMapping("/{login}/new")
    @ResponseBody
    fun addNote(httpEntity: HttpEntity<String>, @PathVariable("login") login: String) : List<Note>{
        if (httpEntity.hasBody()) {
            val newNote =
                jacksonObjectMapper().readValue(httpEntity.body, Note::class.java)
            noteRepository.save(Note(text = newNote.text, author = login))
            return noteRepository.getNotesByUser(login)
        }
        return noteRepository.getNotesByUser(login)
    }

}