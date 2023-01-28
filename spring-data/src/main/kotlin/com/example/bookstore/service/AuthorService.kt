package com.example.bookstore.service;

import com.example.bookstore.entity.Author
import com.example.bookstore.repository.AuthorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthorService @Autowired constructor(val authorRepository: AuthorRepository)
{
    fun fetchAll(): List<Author> {
        return authorRepository.findAll();
    }

    fun findById(id: Long): Author? {
        return authorRepository.findById(id).orElse(null)
    }

    fun findByName(name: String): Author? {
       return authorRepository.findByFirstName(name)
    }

    fun save(author: Author) {
        authorRepository.save(author)
    }

    fun saveAll(authors: List<Author>) {
        authorRepository.saveAll(authors)
    }
}