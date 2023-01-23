package ru.sber.addressbook.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.sber.addressbook.model.Person
import ru.sber.addressbook.service.PersonService

@RestController
@RequestMapping("/public")
internal class PublicController @Autowired constructor(val personService: PersonService) {

    @GetMapping("/list")
    fun list(): ResponseEntity<List<Person>> {
        return ResponseEntity.ok(personService.getAll())
    }
}