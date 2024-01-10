package ru.sber.security.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import ru.sber.security.dto.Student
import ru.sber.security.service.AddressBookService

@RestController
@RequestMapping("/api")
class RestController @Autowired constructor(private val addressBookService: AddressBookService) {

    @PostMapping("/add")
    fun add(@RequestBody student: Student): ResponseEntity<Unit> {
        addressBookService.add(student)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/list")
    fun list(@RequestParam(required = false) query: String?): ResponseEntity<Set<Map.Entry<Long, Student>>> =
        ResponseEntity.ok(addressBookService.list(query))

    @GetMapping("{entityId}/view")
    fun view(@PathVariable entityId: Long): ResponseEntity<Student> =
        addressBookService.view(entityId)?.let { ResponseEntity.ok().body(it) }
            ?: ResponseEntity.notFound().build()

    @PutMapping("{entityId}/edit")
    fun edit(@PathVariable entityId: Long, @RequestBody student: Student): ResponseEntity<Unit> =
        addressBookService.edit(entityId, student)?.let { ResponseEntity.status(HttpStatus.ACCEPTED).build() }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{entityId}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun delete(@PathVariable entityId: Long): ResponseEntity<Unit> =
        addressBookService.delete(entityId)?.let { ResponseEntity.status(HttpStatus.ACCEPTED).build() }
            ?: ResponseEntity.notFound().build()
}
