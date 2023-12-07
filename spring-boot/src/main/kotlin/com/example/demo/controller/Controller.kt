package com.example.demo.controller

import com.example.demo.persistance.Entity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/entity")
class Controller {

    @GetMapping("/{id}")
    fun getEntity(@PathVariable id: Long): Entity = Entity(id, "name of entity")
}