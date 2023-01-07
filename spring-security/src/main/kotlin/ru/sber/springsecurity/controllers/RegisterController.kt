package ru.sber.springsecurity.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/register")
class RegisterController{

    @GetMapping
    fun register(): String {
        return "register"
    }
}