package com.example.demo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller{
    @GetMapping("/give_me_something")
    fun returnSomeThing() = "It is something from rest controller!"
}