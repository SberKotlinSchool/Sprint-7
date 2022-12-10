package com.example.demo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller() {
    @GetMapping("test")
    fun testController() = "Test Page"
}