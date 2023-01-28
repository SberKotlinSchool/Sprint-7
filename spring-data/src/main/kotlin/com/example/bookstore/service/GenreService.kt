package com.example.bookstore.service

import com.example.bookstore.entity.Genre
import com.example.bookstore.repository.GenreRepository
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GenreService @Autowired constructor(val genreRepository: GenreRepository)
{

    fun save(genre: Genre) {
        genreRepository.save(genre)
    }

    fun saveAll(genres: List<Genre>) {
        genreRepository.saveAll(genres)
    }
}