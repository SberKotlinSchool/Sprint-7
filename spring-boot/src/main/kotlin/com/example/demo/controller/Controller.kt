package com.example.demo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    @GetMapping("test")
    fun getRequest(): String {
        return "This is result of get rest request!"
    }
}