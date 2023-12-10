package ru.sber.controller

import mu.KLogging
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import ru.sber.dto.QueryDto
import ru.sber.persistence.AddressEntity
import ru.sber.service.AddressService

@Controller
@RequestMapping("/app")
class MvcController(val addressService: AddressService) {

    companion object : KLogging()

    /**
     * Обработчик страницы list.html - просмотр записей
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/list")
    fun getAddressList(model: Model): String {
        model.addAttribute("addresses", addressService.findAll())
        return "list"
    }

    /**
     * Обработчик страницы list.html - просмотр отфильтрованных записей
     */
    @PostMapping("/list")
    fun getAddressListWithFilter(@ModelAttribute query: QueryDto, model: Model): String {
        model.addAttribute("addresses", addressService.findByQuery(query))
        return "list"
    }

    /**
     * Обработчик добавления новых записей через форму на странице list.html
     */
    @PostMapping("/add")
    fun createAddress(@ModelAttribute addressEntity: AddressEntity, model: Model): String {
        addressService.createAddress(addressEntity)
        model.addAttribute("addresses", addressService.findAll())
        return "list"
    }

    /**
     * Обработчик действия просмотра записи по id.
     */
    @GetMapping("/{id}/view")
    fun getAddress(@PathVariable id: Long, model: Model): String {
        val address = addressService.findById(id)
        model.addAttribute("addresses", listOf(address))
        return "list"
    }

    /**
     * Обработчик обновления записей по id.
     */
    @PostMapping("/{id}/edit")
    fun updateAddress(@PathVariable id: Long, @ModelAttribute addressEntity: AddressEntity, model: Model): String {
        addressService.edit(id, addressEntity)
        model.addAttribute("addresses", addressService.findAll())
        return "list"
    }

    /**
     * Обработчик удаления записей.
     * html-форма умеет только POST-метод, поэтому не http-delete.
     */
    @PostMapping("/{id}/delete")
    fun deleteAddress(@PathVariable id: Long, model: Model): String {
        if (!addressService.delete(id)) {
            logger.warn("delete - not ok")
        }

        model.addAttribute("addresses", addressService.findAll())
        return "list"
    }
}