package org.example.springmvc.controller

import org.example.springmvc.entity.Contact
import org.example.springmvc.service.AddressBookService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class RESTAddressBookController(private val service: AddressBookService)  {

    @GetMapping("/list")
    fun getContacts(@RequestParam(required = false) city: String?): List<Contact> {
        return service.findContactsByCity(city)
    }

    @PostMapping
    fun addContact(@RequestBody contact: Contact) {
        service.add(contact)
    }

    @GetMapping("/{id}")
    fun viewContact(@PathVariable id: Int): Contact? {
        return service.get(id)
    }

    @PutMapping("/{id}")
    fun editContact(@PathVariable id: Int, @RequestBody contact: Contact) {
        service.edit(id, contact)
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun deleteContact(@PathVariable id: Int) {
        service.delete(id)
    }
}