package ru.sber.springsecurity.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.sber.springsecurity.services.NoteService

@Controller
@RequestMapping("/app")
class AppController @Autowired constructor(val noteService: NoteService) {

    @GetMapping
    fun app(modelMap: ModelMap): String {
        modelMap.addAttribute("notes", noteService.findAllNotes())
        return "app"
    }

    @PostMapping
    fun processPost(modelMap: ModelMap): String {
        modelMap.addAttribute("notes", noteService.findAllNotes())
        return "app"
    }
}