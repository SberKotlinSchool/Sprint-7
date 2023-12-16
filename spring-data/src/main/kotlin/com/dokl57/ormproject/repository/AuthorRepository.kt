package com.dokl57.ormproject.repository

import com.dokl57.ormproject.model.Author
import org.springframework.data.repository.CrudRepository

interface AuthorRepository : CrudRepository<Author, Long>
