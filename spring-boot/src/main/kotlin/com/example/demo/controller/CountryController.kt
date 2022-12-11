package com.example.demo.controller

import com.example.demo.persistance.Country
import com.example.demo.persistance.CountryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
@RestController
class CountryController(
    @Autowired
    val repository: CountryRepository
) {

    @GetMapping("/country/{id}")
    fun getOne(@PathVariable id: Long): ResponseEntity<Country> {
        val country = repository.findById(id)
        return ResponseEntity.ok(country.get())
    }
}