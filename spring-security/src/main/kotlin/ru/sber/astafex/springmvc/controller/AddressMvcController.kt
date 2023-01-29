package ru.sber.astafex.springmvc.controller

import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import ru.sber.astafex.springmvc.entity.Address
import ru.sber.astafex.springmvc.service.AddressService

@Controller
@RequestMapping("/app")
class AddressMvcController(private val service: AddressService) {

    @GetMapping("/list")
    fun getAddressList(@RequestParam(required = false) query: String?, model: Model): String {
        model.addAttribute("addressList", service.getAddresses(query))
        return "list"
    }

    @GetMapping("/{id}/view")
    fun viewAddress(@PathVariable id: Long, model: Model): String {
        model.addAttribute("address", service.get(id))
        return "view"
    }

    @GetMapping("/{id}/edit")
    fun getFormEditAddress(@PathVariable id: Long, model: Model): String {
        model.addAttribute("address", service.get(id))
        return "edit"
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/edit")
    fun editAddress(@PathVariable id: Long, @ModelAttribute address: Address, model: Model): String {
        service.edit(id, address)
        return "redirect:/app/list"
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/delete")
    fun deleteAddress(@PathVariable id: Long): String {
        service.delete(id)
        return "redirect:/app/list"
    }

    @GetMapping("/add")
    fun getFormNewAddress(@ModelAttribute address: Address, model: Model): String {
        model.addAttribute("address", address)
        return "add"
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    fun addNewAddress(@ModelAttribute address: Address): String {
        service.add(address)
        return "redirect:/app/list"
    }
}