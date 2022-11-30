package ru.company.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import ru.company.model.Client
import ru.company.service.AddressBookService
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class AddressBookRestController @Autowired constructor(val service: AddressBookService) {


    @PostMapping("/add")
    fun addClient(@Valid @RequestBody client: Client): ResponseEntity<Client> {
        service.add(client)
        return ResponseEntity.ok(client)
    }

    @GetMapping("/list")
    fun getClients(
        @RequestParam(required = false) fio: String?,
        @RequestParam(required = false) address: String?,
        @RequestParam(required = false) phone: String?,
        @RequestParam(required = false) email: String?,
        model: Model
    ): ResponseEntity<List<Client>> {
        return ResponseEntity.ok(service.getClients(fio, address, phone, email))
    }

    @GetMapping("/{id}/view")
    fun viewClient(@PathVariable id: Long): ResponseEntity<Client?> {
        return ResponseEntity.ok(service.getClientById(id).get())
    }

    @PutMapping("/{id}/edit", produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun editClient(@Valid @RequestBody client: Client): ResponseEntity<Client?> {
        service.updateClient(client)
        return ResponseEntity.ok(client)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    fun deleteClient(@PathVariable id: Long) {
        service.deleteClient(id)
    }
}