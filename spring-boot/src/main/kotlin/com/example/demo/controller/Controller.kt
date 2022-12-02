package com.example.demo.controller

import com.example.demo.persistance.Entity
import com.example.demo.persistance.EntityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.random.Random

@RestController
@RequestMapping("/*")
class Controller @Autowired constructor(private val entityRepository: EntityRepository){

    @GetMapping
    fun index(): List<Entity> {
        val random = Random.nextInt(1, 5)
        for (index in 0 until random) {
            entityRepository.save(Entity(name = "name$index"))
        }
        return entityRepository.findAll()
    }
}