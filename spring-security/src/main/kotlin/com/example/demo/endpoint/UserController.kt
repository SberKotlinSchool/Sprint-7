package com.example.demo.endpoint

import com.example.demo.endpoint.dto.RequestData
import com.example.demo.persistance.Author
import com.example.demo.persistance.UserDetailsAdapter
import com.example.demo.service.AuthorService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api")
class UserController(val service: AuthorService) {

    @GetMapping("/")
    fun index(): String = "redirect:/api/list"

    @GetMapping("/list")
    fun list(model: Model): String {
        model.addAttribute("authors", service.getAuthors())
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
        service.add(Author(0, requestData.firstName, requestData.secondName, owner), user.username)
        return "redirect:/api/list"
    }

    @GetMapping("/view/{id}")
    fun view(model: Model, @PathVariable("id") id: Long): String {
        model.addAttribute("author", service.getById(id))
        return "view"
    }

    @PostMapping("/view/{id}")
    fun update(requestData: RequestData, @PathVariable("id") id: Long): String {
        service.update(Author(id, requestData.firstName, requestData.secondName, 0))
        return "redirect:/api/view/$id"
    }

    @GetMapping("/remove/{id}")
    fun remove(model: Model, @PathVariable("id") id: Long): String {
        model.addAttribute("author", service.remove(id))
        return "redirect:/api/list"
    }
}