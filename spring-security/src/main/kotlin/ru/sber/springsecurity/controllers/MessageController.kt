package ru.sber.springsecurity.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import ru.sber.springsecurity.entities.Note
import ru.sber.springsecurity.services.NoteService

@Controller
@RequestMapping("/message")
class MessageController @Autowired constructor(val noteService: NoteService) {

    @PostMapping("/new")
    fun newMessage(@ModelAttribute note: Note): String {
        noteService.saveNote(note)
        return "redirect:/app"
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete/{id}")
    fun deleteMessage(@PathVariable("id") id: String): String {
        noteService.deleteNoteById(id.toLong())
        return "redirect:/app"
    }

    @GetMapping("/edit/{id}")
    fun editMessage(@PathVariable("id") id: String, model: Model): String {
        var activeUser = ""
        val principal = SecurityContextHolder.getContext().authentication.principal
        activeUser = if (principal is UserDetails) {
            principal.username
        } else {
            principal as String
        }
        model.addAttribute("user", activeUser)
        model.addAttribute("id", id)
        model.addAttribute("note", noteService.findNoteById(id.toLong()))
        return "message"
    }

    @PostMapping("/edit/{id}")
    fun processEditMessage(@PathVariable("id") id: String, @ModelAttribute note: Note, model: Model): String {
        noteService.editNoteById(id.toLong(), note)
        model.addAttribute("notes", noteService.findAllNotes())
        return "redirect:/app"
    }

}