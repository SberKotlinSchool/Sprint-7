package ru.sber.spring.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {
    @GetMapping
    fun getMainPage(model: Model): String {
        return "main"
    }
}
