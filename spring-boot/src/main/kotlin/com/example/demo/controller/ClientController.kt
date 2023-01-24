package com.example.demo.controller

import com.example.demo.service.ClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class ClientController() {

    lateinit var service : ClientService
        @Autowired set
//
//    @GetMapping("/{id}/phone")
//    fun getPhoneNumber(@PathVariable id: Long) = ResponseEntity.ok(service.getPhoneNumberById(id))


    @GetMapping("/{id}/phone")
    fun getPhoneNumber(@PathVariable id: Long): ResponseEntity<String> {
        return ResponseEntity.ok(service.getPhoneNumberById(id))
    }

}