package com.example.demo.controller

import com.example.demo.persistance.BookEntity
import com.example.demo.persistance.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BookController @Autowired constructor(val bookRepository: BookRepository) {
    @GetMapping("/list")
    fun getBookById(id: Long): ResponseEntity<BookEntity> {
        return ResponseEntity.ok(bookRepository.getById(id))
    }
}
