package com.example.bookstore.repository

import com.example.bookstore.entity.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : JpaRepository<Author, Long> {

    fun findByFirstName(name: String): Author?

}