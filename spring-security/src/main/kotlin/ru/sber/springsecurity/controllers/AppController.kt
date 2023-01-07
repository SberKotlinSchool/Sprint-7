package ru.sber.springsecurity.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.sber.springsecurity.services.NoteService

@Controller
@RequestMapping("/app")
class AppController @Autowired constructor(val noteService: NoteService) {

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping
    fun app(modelMap: ModelMap): String {
        var activeUser = ""
        val principal = SecurityContextHolder.getContext().authentication.principal

        activeUser = if (principal is UserDetails) {
            principal.username
        } else {
            principal as String
        }
        modelMap.addAttribute("user", activeUser)
        modelMap.addAttribute("notes", noteService.findAllNotes())
        return "app"
    }
}