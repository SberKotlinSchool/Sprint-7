package ru.sber.addressbook.controllers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import ru.sber.addressbook.filters.LogsFilter
import ru.sber.addressbook.models.CLientsInfo
import ru.sber.addressbook.services.AddressbookService


@Controller
@RequestMapping("app")
class AddressbookMvcController @Autowired constructor(val service: AddressbookService) {

    private val logger = LoggerFactory.getLogger(LogsFilter::class.java)

    @GetMapping("/list")
    fun list(@RequestParam(required = false) query: String?, model: Model): String {
        model.addAttribute("list", service.list(query))
        return "list"
    }

    @GetMapping("/add")
    fun add() = "add"

    @PostMapping("/add")
    fun add(@ModelAttribute("client") client: CLientsInfo, model: Model): String {
        service.add(client)
        logger.info("${client.name}\t${client.phone}")
        return "redirect:/app/list"
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/delete")
    fun delete(@PathVariable id: String, model: Model): String {
        return service.view(id)?.let {
            "delete"
        } ?: "redirect:/app/list"
    }

    @PostMapping("/{id}/delete")
    fun delete(@PathVariable id: String): String {
        service.delete(id)

        return "redirect:/app/list"
    }

    @GetMapping("/{id}/view")
    fun view(@PathVariable id: String, model: Model): String {
        model.addAttribute("client", service.view(id))
        return "view"
    }

    @GetMapping("/{id}/edit")
    fun edit(@PathVariable id: String, model: Model): String {
        return service.view(id)?.let {
            model.addAttribute("client", it)
            "edit"}?: "redirect:/app/list"
    }

    @PostMapping("/{id}/edit")
    fun edit(@ModelAttribute("client") client: CLientsInfo, @PathVariable id: String): String {
        println(id)
        println(client)
        service.edit(id, client)
        return "redirect:/app/list"
    }
}