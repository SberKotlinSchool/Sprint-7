package com.example.demo.controller

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(@Autowired val entityRepository: EntityRepository) {
    @GetMapping("hello")
    fun hello() = "Hello!"

    @GetMapping("/{id}/view")
    fun view(@PathVariable id: Long): Entity? {
        return entityRepository.getById(id)
    }
}
