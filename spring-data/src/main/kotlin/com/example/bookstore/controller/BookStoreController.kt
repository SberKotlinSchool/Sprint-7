package com.example.bookstore.controller

import com.example.bookstore.entity.Book
import com.example.bookstore.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BookStoreController @Autowired constructor(var bookService: BookService){

    @GetMapping("/get/books")
    fun getAllBooks() : List<Book>
    {
        return bookService.fetchAll();
    }
}