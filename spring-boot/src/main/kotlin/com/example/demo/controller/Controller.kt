package com.example.demo.controller

import com.example.demo.persistance.Entity
import com.example.demo.service.DatabaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller @Autowired constructor(private val service: DatabaseService) {

    @GetMapping("/get/{id}")
    fun getById(@PathVariable id: Long): Entity =
        service.getById(id)
}
