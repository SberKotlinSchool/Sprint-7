package ru.sber.app.endpoint

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.sber.app.endpoint.dto.RequestData
import ru.sber.app.entity.AddressBook
import ru.sber.app.entity.UserDetailsAdapter
import ru.sber.app.service.AddressBookService

@Controller
@RequestMapping("/app")
class UserController(val service: AddressBookService) {

    @GetMapping("/")
    fun index(): String = "redirect:/app/list"

    @GetMapping("/list")
    fun list(model: Model): String {
        model["notes"] = service.getAddressBook()
        return "list"
    }

    @GetMapping("/add")
    fun add(): String {
        return "add"
    }

    @PostMapping("/add")
    fun add(requestData: RequestData, auth: Authentication): String {
        val user = (auth.principal as UserDetailsAdapter).user
        var owner = user.id
        if (requestData.public == true) {
            owner = 0
        }
        service.add(AddressBook(0, requestData.firstName, requestData.lastName, requestData.city, owner), user.username)
        return "redirect:/app/list"
    }

    @PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'ru.sber.app.entity.AddressBook','DELETE') " +
            "or hasPermission(#id, 'ru.sber.app.entity.AddressBook','READ')")
    @GetMapping("/view/{id}")
    fun view(model: Model, @PathVariable("id") id: Long): String {
        model.addAttribute("note", service.getById(id))
        return "view"
    }

    @PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'ru.sber.app.entity.AddressBook','DELETE')")
    @PostMapping("/view/{id}")
    fun update(@ModelAttribute requestData: RequestData, @PathVariable("id") id: Long): String {
        service.update(AddressBook(id, requestData.firstName, requestData.lastName, requestData.city, 0))
        return "redirect:/app/view/$id"
    }

    @PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'ru.sber.app.entity.AddressBook','DELETE')")
    @GetMapping("/remove/{id}")
    fun remove(model: Model, @PathVariable("id") id: Long): String {
        model.addAttribute("note", service.remove(id))
        return "redirect:/app/list"
    }
}