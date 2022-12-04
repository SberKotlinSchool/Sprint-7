package com.example.demo.controller

import com.example.demo.persistance.Author
import com.example.demo.persistance.AuthorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/app")
@RestController
class Controller {
    @Autowired
    private lateinit var repository: AuthorRepository

    @GetMapping("/author")
    fun getAuthor(@RequestParam id: Long): ResponseEntity<Author> {
        val dataEntity = repository.getById(id)
        return ResponseEntity.ok(dataEntity)
    }
}