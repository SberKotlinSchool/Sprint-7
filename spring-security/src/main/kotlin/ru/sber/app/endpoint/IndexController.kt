package ru.sber.app.endpoint

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import ru.sber.app.service.AddressBookService

@Controller
class IndexController(val service: AddressBookService) {

    @GetMapping("/")
    fun index(): String = "redirect:/app/list"

}