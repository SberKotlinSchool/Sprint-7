package com.example.demo.service

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

interface DatabaseService {

    fun getById(id: Long): Entity
}

@Service
class DatabaseServiceImpl @Autowired constructor(private val repository: EntityRepository) : DatabaseService {

    override fun getById(id: Long): Entity =
        repository.findById(id).get()
}
