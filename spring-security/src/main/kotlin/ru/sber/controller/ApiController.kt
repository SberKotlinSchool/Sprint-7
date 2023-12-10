package ru.sber.controller

import org.springframework.web.bind.annotation.*
import ru.sber.dto.QueryDto
import ru.sber.persistence.AddressEntity
import ru.sber.service.AddressService

@RestController
@RequestMapping("/api")
class ApiController(val addressService: AddressService) {


    @PostMapping("add")
    fun add(@RequestBody addressEntity: AddressEntity) {
        addressService.createAddress(addressEntity)
    }

    @GetMapping("list")
    fun list(): List<AddressEntity> {
        return addressService.findAll()
    }

    @PostMapping("list")
    fun listWithFilter(@RequestBody queryDto: QueryDto): List<AddressEntity> {
        return addressService.findByQuery(queryDto)
    }

    @GetMapping("{id}/view")
    fun viewById(@PathVariable id: Long): AddressEntity? {
        return addressService.findById(id)
    }

    @PutMapping("{id}/edit")
    fun editById(@PathVariable id: Long, @RequestBody addressEntity: AddressEntity): Boolean {
        return addressService.edit(id, addressEntity)
    }

    @DeleteMapping("{id}/delete")
    fun deleteById(@PathVariable id: Long): Boolean {
        return addressService.delete(id)
    }

}