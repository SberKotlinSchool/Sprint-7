package ru.sber.springsecurity.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.sber.springsecurity.model.AddressBookRow
import ru.sber.springsecurity.service.AddressBookService

@Controller
@RequestMapping("/app")
class MvcController(private val addressBookService: AddressBookService) {

    @PostMapping("/add")
    fun add(@ModelAttribute newRow: AddressBookRow): String {
        addressBookService.add(newRow)
        return "redirect:/app/list"
    }

    @GetMapping("/{id}/view")
    fun view(@PathVariable id: String, model: Model): String {
        model.addAttribute("row", addressBookService.get(id))
        return "view"
    }

    @GetMapping("/list")
    fun list(model: Model): String {
        model.addAttribute("bookRows", addressBookService.getAll())
        return "list"
    }

    @PostMapping("/{id}/edit")
    fun edit(@ModelAttribute updatedRow: AddressBookRow, @PathVariable id: String, model: Model): String {
        if (id == updatedRow.name) {
            addressBookService.edit(updatedRow.name, updatedRow.address)
        }
        return "redirect:/app/$id/view"
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(@PathVariable id: String): String {
        addressBookService.delete(id)
        return "redirect:/app/list"
    }
}
