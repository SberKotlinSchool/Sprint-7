package com.dokl57.springmvc.controller

import com.dokl57.springmvc.model.Entry
import com.dokl57.springmvc.service.AddressBookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class RestApiController @Autowired constructor(val addressBookService: AddressBookService) {

    @PostMapping("/add")
    fun addEntry(@RequestBody entry: Entry): ResponseEntity<Unit> {
        return ResponseEntity.ok(addressBookService.add(entry))
    }

    @GetMapping("/list")
    fun getEntries(@RequestParam(required = false) query: String?): ResponseEntity<List<Entry>> {
        return ResponseEntity.ok(addressBookService.getEntries(query))
    }

    @GetMapping("{id}/view")
    fun viewEntry(@PathVariable id: Long): ResponseEntity<Entry> {
        return ResponseEntity.ok().body(
            addressBookService.getEntryById(id)
                ?: return ResponseEntity.notFound().build()
        )
    }

    @PutMapping("{id}/edit")
    fun editEntry(@PathVariable id: Long, @RequestBody entry: Entry): Any {
        return ResponseEntity.ok().body(
            addressBookService.updateEntry(id, entry)
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/delete")
    fun deleteEntry(@PathVariable id: Long): Any {
        return ResponseEntity.ok().body(
            addressBookService.deleteEntry(id)
        )
    }
}