package ru.sber.springsec.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import ru.sber.springsec.service.BookRow
import ru.sber.springsec.service.BookService
import org.springframework.security.access.prepost.PreAuthorize
@Controller
@RequestMapping("/app")
class MvcController(private val bookService: BookService) {

    @PostMapping("/add")
    fun add(@ModelAttribute newRow: BookRow): String {
        bookService.add(newRow)
        return "redirect:/app/list"
    }

    @GetMapping("/{id}/view")
    fun view(@PathVariable id: String, model: Model): String {
        model.addAttribute("row", bookService.get(id))
        return "view"
    }

    @GetMapping("/list")
    fun list(model: Model): String {
        model.addAttribute("bookRows", bookService.getAll())
        return "list"
    }

    @PostMapping("/{id}/edit")
    fun edit(@ModelAttribute updatedRow: BookRow, @PathVariable id: String, model: Model): String {
        if (id == updatedRow.name)
            bookService.edit(updatedRow.name, updatedRow.address)
        return "redirect:/app/$id/view"
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(@PathVariable id: String): String {
        bookService.delete(id)
        return "redirect:/app/list"
    }
}