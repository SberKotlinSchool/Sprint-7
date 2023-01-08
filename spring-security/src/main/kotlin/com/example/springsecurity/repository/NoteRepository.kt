package com.example.springsecurity.repository

import com.example.springsecurity.entity.Note
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface NoteRepository: JpaRepository<Note, Long> {

    @Query("SELECT n FROM Note n WHERE n.author = :login")
    fun getNotesByUser(@Param("login") login: String) : List<Note>

    @Modifying
    @Transactional
    @Query("UPDATE Note n SET n.text = :newText WHERE n.id = :id")
    fun updateNoteById(@Param("id") id: Long, @Param("newText") text: String)
}