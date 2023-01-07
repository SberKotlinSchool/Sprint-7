package ru.sber.springsecurity.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import ru.sber.springsecurity.entities.Note
import ru.sber.springsecurity.services.NoteService

@Controller
@RequestMapping("/message")
class MessageController @Autowired constructor(val noteService: NoteService) {

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/new")
    fun newMessage(@ModelAttribute note: Note): String {
        noteService.saveNote(note)
        return "redirect:/app"
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/delete/{id}")
    fun deleteMessage(@PathVariable("id") id: String): String {
        noteService.deleteNoteById(id.toLong())
        return "redirect:/app"
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/edit/{id}")
    fun editMessage(@PathVariable("id") id: String, model: Model): String {
        model.addAttribute("id", id)
        model.addAttribute("notes", noteService.findAllNotes())
        return "message"
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/edit/{id}")
    fun processEditMessage(@PathVariable("id") id: String, @ModelAttribute note: Note, model: Model): String {
        noteService.editNoteById(id.toLong(), note)
        model.addAttribute("notes", noteService.findAllNotes())
        return "redirect:/app"
    }

}