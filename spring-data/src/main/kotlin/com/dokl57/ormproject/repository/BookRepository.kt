package com.dokl57.ormproject.repository

import com.dokl57.ormproject.model.Book
import org.springframework.data.repository.CrudRepository

interface BookRepository : CrudRepository<Book, Long>
