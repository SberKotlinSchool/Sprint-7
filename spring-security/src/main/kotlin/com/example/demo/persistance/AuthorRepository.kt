package com.example.demo.persistance

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : JpaRepository<Author, Long>{
    fun findAllBySecondName(secondName: String?): List<Author?>?

    @Query("SELECT p FROM Author p WHERE p.owner = ?1 or p.owner = 0")
    fun findByOwner(owner: Long): Iterable<Author>
}