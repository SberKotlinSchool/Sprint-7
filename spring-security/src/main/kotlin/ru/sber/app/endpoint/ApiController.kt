package ru.sber.app.endpoint

import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import ru.sber.app.endpoint.dto.RequestData
import ru.sber.app.entity.ADDRESSBOOK
import ru.sber.app.entity.UserDetailsAdapter
import ru.sber.app.service.AddressBookService

@RestController
@RequestMapping(path=["/api"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE])
class ApiController(val service: AddressBookService) {

    @GetMapping("/list")
    fun list(): Iterable<ADDRESSBOOK> {
        return service.getAddressBook()
    }

    @PostMapping("/add")
    fun add(@RequestBody requestData: RequestData, auth: Authentication) {
        val user = (auth.principal as UserDetailsAdapter).user
        var owner = user.id
        if (requestData.public == true) {
            owner = 0
        }
        service.add(ADDRESSBOOK(0, requestData.firstName, requestData.lastName, requestData.city, owner), user.username)
    }

    @PutMapping("/edit/{id}")
    fun update(@RequestBody requestData: RequestData, @PathVariable("id") id: Long) {
        service.update(ADDRESSBOOK(id, requestData.firstName, requestData.lastName, requestData.city, 0))
    }

    @GetMapping("/view/{id}")
    fun view(@PathVariable("id") id: Long): ADDRESSBOOK {
        return service.getById(id)
    }

    @DeleteMapping("/remove/{id}")
    fun remove(@PathVariable("id") id: Long) {
        service.remove(id)
    }
}