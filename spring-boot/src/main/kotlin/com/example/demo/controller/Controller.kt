package com.example.demo.controller

import com.example.demo.persistance.Entity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    @GetMapping("/get")
    fun get(@RequestParam id: Long): Entity {
        return Entity(id, "NAME")
    }
}