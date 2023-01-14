package com.example.demo.controller

import com.example.demo.persistance.BookEntity
import com.example.demo.persistance.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BookController {
    @GetMapping("/sleep")
    fun goodNight()="Good night!"
}
