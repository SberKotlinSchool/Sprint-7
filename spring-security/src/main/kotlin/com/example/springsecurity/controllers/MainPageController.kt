package com.example.springsecurity.controllers

import com.example.springsecurity.service.NoteService
import com.example.springsecurity.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/mainpage")
class MainPageController @Autowired constructor(
    private val noteService: NoteService,
    private val userService: UserService
){

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    fun toMainPage(model : Model) : String {
        val userName = userService.getUserFromContext()
        model.addAttribute("login", userName)
        model.addAttribute("notes", noteService.findAllNotesByAuthor(userName))
        return "mainpage"
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    fun toMainPage(modelMap: ModelMap) : String {
        val userName = userService.getUserFromContext()

        modelMap.addAttribute("login", userName)
        modelMap.addAttribute("notes", noteService.findAllNotesByAuthor(userName))
        return "mainpage"
    }
}