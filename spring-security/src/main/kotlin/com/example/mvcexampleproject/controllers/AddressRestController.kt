package com.example.mvcexampleproject.controllers

import com.example.mvcexampleproject.domain.Address
import com.example.mvcexampleproject.services.AddressService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest")
class AddressRestController(private val service: AddressService) {

    @GetMapping("/list")
    fun getAddressList(@RequestParam(required = false) query: String?): List<Address> {
        return service.getAddresses(query)
    }

    @PreAuthorize("hasRole('API') or hasRole('ADMIN')")
    @PostMapping
    fun addNewAddress(@RequestBody address: Address) {
        service.add(address)
    }

    @GetMapping("/{id}")
    fun viewAddress(@PathVariable id: Long): Address? {
        return service.get(id)
    }

    @PutMapping("/{id}")
    fun editAddress(@PathVariable id: Long, @RequestBody address: Address) {
        service.edit(id, address)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteAddress(@PathVariable id: Long) {
        service.delete(id)
    }

}