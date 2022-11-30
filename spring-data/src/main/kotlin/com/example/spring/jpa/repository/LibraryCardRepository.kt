package com.example.spring.jpa.repository

import com.example.spring.jpa.entity.LibraryCard
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LibraryCardRepository : CrudRepository<LibraryCard, Long> {
   fun findAllById(id: Long): List<LibraryCard>
}
