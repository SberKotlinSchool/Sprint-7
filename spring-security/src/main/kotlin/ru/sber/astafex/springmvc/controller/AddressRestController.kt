package ru.sber.astafex.springmvc.controller

import org.springframework.web.bind.annotation.*
import ru.sber.astafex.springmvc.entity.Address
import ru.sber.astafex.springmvc.service.AddressService

@RestController
@RequestMapping("/rest")
class AddressRestController(private val service: AddressService) {
    @GetMapping("/list")
    fun getAddressList(@RequestParam(required = false) query: String?): List<Address> {
        return service.getAddresses(query)
    }

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

    @DeleteMapping("/{id}")
    fun deleteAddress(@PathVariable id: Long) {
        service.delete(id)
    }
}