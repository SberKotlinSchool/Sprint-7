package com.example.demo.controller

import com.example.demo.entity.Address
import com.example.demo.service.AddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class RestController(@Autowired private val addressBook: AddressService) {
    @GetMapping("/list")
    fun view(@RequestParam(required = false) name: String?) = ResponseEntity.ok(
//        if (name.isNullOrEmpty())
            addressBook.getAll()
//        else
//            addressBook.getAll().filter { it.name == name }
            )

    @PostMapping("/add")
    fun add(@RequestBody address: Address) = ResponseEntity.ok(addressBook.add(address))

    @DeleteMapping("/{id}/delete")
    fun delete(@PathVariable id: Long) = ResponseEntity.ok(addressBook.delete(id))

    @PutMapping("/{id}/edit")
    fun edit(@PathVariable id: Long, @RequestBody address: Address) = ResponseEntity.ok(
        addressBook.update(id, address)
    )

    @GetMapping("/{id}/view")
    fun detail(@PathVariable id: Long) = ResponseEntity.ok(
        addressBook.get(id)
    )
}