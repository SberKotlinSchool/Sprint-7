package com.example.springsecurity.controllers

import com.example.springsecurity.entity.Note
import com.example.springsecurity.service.NoteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/note")
class NoteController @Autowired constructor(private val noteService: NoteService) {

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/new")
    fun addNewNote(@RequestParam("text") newNote: String, @RequestParam("login") login: String, model: Model): String {
        noteService.saveNote(Note(text = newNote, author = login))
        model.addAttribute("login", login)
        model.addAttribute("notes", noteService.findAllNotesByAuthor(login))
        return "mainpage"
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/edit")
    fun editNote(@RequestParam("id") id: String, @RequestParam("note") text: String, @RequestParam("login") login: String, model: Model): String {
        model.addAttribute("login", login)
        model.addAttribute("notes", noteService.editNote(id.toLong(), text))

        return "mainpage"
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    fun deleteNote(@RequestParam("id") id: String, @RequestParam("login") login: String, model: Model): String {
        noteService.deleteNoteById(id.toLong())
        model.addAttribute("login", login)
        model.addAttribute("notes", noteService.findAllNotesByAuthor(login))
        return "mainpage"
    }
}