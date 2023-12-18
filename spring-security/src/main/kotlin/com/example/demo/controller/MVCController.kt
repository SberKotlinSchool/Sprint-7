package com.example.demo.controller

import com.example.demo.entity.Address
import com.example.demo.service.AddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/app")
class MVCController(@Autowired private val addressBook: AddressService) {
    @GetMapping("/list")
    fun view(@RequestParam(required = false) name: String?, model: Model): String {
//        val result = if (name.isNullOrBlank()) addressBook.getAll()
//        else addressBook.getAll().filter { it.name == name }
        val result = addressBook.getAll()
        model.addAttribute("address", result)
        return "list"
    }

    @GetMapping("/add")
    fun add(model: Model): String {
        model.addAttribute("address", Address())
        return "add"
    }

    @PostMapping("/add")
    fun add(@ModelAttribute address: Address): String {
        addressBook.add(address)
        return "redirect:/app/list"
    }

    @GetMapping("/{id}/delete")
    fun delete(@PathVariable id: Long): String {
        addressBook.delete(id)
        return "redirect:/app/list"
    }

    @GetMapping("/{id}/edit")
    fun edit(@PathVariable id: Long, model: Model): String {
        model.addAttribute("address", addressBook.get(id))
        return "edit"
    }

    @PostMapping("/{id}/edit")
    fun edit(@PathVariable id: Long, @ModelAttribute address: Address): String {
        addressBook.update(id, address)
        return "redirect:/app/list"
    }

    @GetMapping("/{id}/view")
    fun detail(@PathVariable id: Long, model: Model): String {
        model.addAttribute("address", addressBook.get(id))
        return "view"
    }
}
