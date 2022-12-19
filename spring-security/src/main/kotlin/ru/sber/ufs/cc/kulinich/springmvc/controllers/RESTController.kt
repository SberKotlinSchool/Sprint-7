package ru.sber.ufs.cc.kulinich.springmvc.controllers

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import ru.sber.ufs.cc.kulinich.springmvc.filters.LoggingFilter
import ru.sber.ufs.cc.kulinich.springmvc.models.AddressBookModel
import ru.sber.ufs.cc.kulinich.springmvc.services.AddressBookService
import java.util.*
import kotlin.collections.ArrayList


@RestController
@RequestMapping("/api/*")
class RESTController {

    private val logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    lateinit var addrBook : AddressBookService
        @Autowired set

    private var restTemplate: RestTemplate? = null


    @PostMapping("/add", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addCustomer( @RequestBody form: AddressBookModel): ResponseEntity<AddressBookModel> {
        addrBook.add(form)
        return ResponseEntity.ok(form)
    }

    @GetMapping( "/list", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCustomers(model: Model): ResponseEntity<List<AddressBookModel>> {
        return ResponseEntity.ok(addrBook.getAll())
    }

    @GetMapping("/{id}/view", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun viewCustomer(@PathVariable id: Int): ResponseEntity<AddressBookModel?> {
        return ResponseEntity.ok(addrBook.getById(id))
    }

    @PostMapping("/{id}/edit", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun editCustomer(
        @PathVariable id: Int,
        @RequestBody form: AddressBookModel,
        result: BindingResult
    ): ResponseEntity<AddressBookModel?> {
        addrBook.edit(id, form)
        return ResponseEntity.ok(form)
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    fun deleteCustomer(@PathVariable id: Int) {
        addrBook.delete(id)
    }

}