package ru.sber.springsecurity.controllers.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/login")
class LoginRestController{

    @GetMapping
    fun login (@RequestParam(value = "name", defaultValue = "") userName: String,
              @RequestParam(value = "pass", defaultValue = "") password: String): Map<String, String> {
        val map = HashMap<String, String>()
        val ans = "success"

        map["status"] = ans
        return map
    }
}