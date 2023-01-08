package com.example.demo.controller

import com.example.demo.service.ClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ClientController(@Autowired val service : ClientService) {

    @GetMapping("/{id}/phone")
    fun getPhoneNumber(@PathVariable id: Long) = ResponseEntity.ok(service.getPhoneNumberById(id))

}