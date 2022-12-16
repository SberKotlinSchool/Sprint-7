package com.sbuniver.homework.controller

import com.sbuniver.homework.dto.AddForm
import com.sbuniver.homework.dto.AddressBook
import com.sbuniver.homework.dto.AddressDto
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@Controller
@RequestMapping("/app")
class AppController {

    @Autowired
    lateinit var addressBook: AddressBook

    @GetMapping("/list")
    fun list(model: Model): String {
        logger.info { "income get request to /app/list" }
        model.addAttribute("addresses", addressBook.addresses)
        return "list"
    }

    @GetMapping("/{id}/view")
    fun view(model: Model, @PathVariable id: Int): String {
        model.addAttribute(
            "address",
            if (addressBook.get(id) == null) "No address found with id=$id"
            else addressBook.get(id).toString()
        )
        return "viewOne"
    }

    @PutMapping("/{id}/edit")
    fun editDo(
        @PathVariable id: Int,
        @ModelAttribute addForm: AddForm,
        response: HttpServletResponse
    ) {
        println(addForm)
        val addressDto = AddressDto(
            id,
            addForm.name!!,
            addForm.city!!,
            addForm.street!!,
            addForm.home!!.toInt(),
        )
        addressBook.update(addressDto)
        response.sendRedirect("/app/list")
    }

    @PostMapping("/add")
    fun add(
        @ModelAttribute addForm: AddForm,
        response: HttpServletResponse
    ) {
        addressBook.add(
            AddressDto(
                addressBook.maxId() + 1,
                addForm.name!!,
                addForm.city!!,
                addForm.street!!,
                addForm.home!!.toInt(),
            )
        )
        response.sendRedirect("/app/list")
    }

    @GetMapping("/{id}/edit")
    fun editPage(model: Model, @PathVariable id: Int): String {
        model.addAttribute("address", addressBook.get(id))
        return "edit"
    }

    @DeleteMapping("/{id}/delete")
    fun deleteDo(
        @PathVariable id: Int,
        response: HttpServletResponse
    ) {
        addressBook.delete(addressBook.get(id)!!)
        response.sendRedirect("/app/list")
    }

    @GetMapping("/{id}/delete")
    fun deletePage(model: Model, @PathVariable id: Int): String {
        model.addAttribute("address", addressBook.get(id))
        return "delete"
    }

    companion object {
        val logger = KotlinLogging.logger {}
    }
}