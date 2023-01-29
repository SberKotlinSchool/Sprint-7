package ru.morningcake.addressbook.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import ru.morningcake.addressbook.dto.NoteDto
import ru.morningcake.addressbook.entity.Note
import ru.morningcake.addressbook.entity.User
import ru.morningcake.addressbook.service.NoteService
import ru.morningcake.addressbook.utils.AppUtils
import java.util.*

@Controller
@RequestMapping("/app")
class AppController @Autowired constructor(private val service: NoteService, private val appUtils : AppUtils) {

    /** После логина  */
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping
    fun showClientsAfterLogin(@AuthenticationPrincipal self: User, model: Model): String? {
        return if (self.isAdmin()) {
            "redirect:/user/admin_panel"
        } else {
            "redirect:/app/list"
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/list")
    fun fromAuth(model: Model, @AuthenticationPrincipal user: User): String {
        appUtils.addBaseUrlAndUserNameToModel(model, user.name!!)
        addNotesToModel(model)
        return "book"
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/list")
    fun showBook(model: Model, @AuthenticationPrincipal user: User): String {
        appUtils.addBaseUrlAndUserNameToModel(model, user.name!!)
        addNotesToModel(model)
        return "book"
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/filter")
    fun showFilteredBook(model: Model, @AuthenticationPrincipal user: User, filter : String): String {
        appUtils.addBaseUrlAndUserNameToModel(model, user.name!!)
        addNotesToModel(model, filter)
        return "book"
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/create")
    fun createNoteForm(model: Model, @AuthenticationPrincipal user: User): String {
        appUtils.addBaseUrlAndUserNameToModel(model, user.name!!)
        return "note_create"
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/create")
    fun createNote(model: Model, @AuthenticationPrincipal user: User, dto : NoteDto): String {
        appUtils.addBaseUrlAndUserNameToModel(model, user.name!!)
        val created = service.create(dto)
        addNoteToModel(model, created)
        return "note"
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/note/{id}")
    fun showNote(model: Model, @AuthenticationPrincipal user: User, @PathVariable id : UUID): String {
        appUtils.addBaseUrlAndUserNameToModel(model, user.name!!)
        val note = service.getById(id)
        addNoteToModel(model, note)
        return "note"
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/note/{id}/update")
    fun updateNote(model: Model, @AuthenticationPrincipal user: User, dto : NoteDto, @PathVariable id : UUID): String {
        appUtils.addBaseUrlAndUserNameToModel(model, user.name!!)
        val updated = service.update(id, dto)
        addNoteToModel(model, updated)
        return "note"
    }

    @PreAuthorize("hasAuthority('DELETER')")
    @GetMapping("/note/{id}/delete")
    fun deleteNote(model: Model, @AuthenticationPrincipal user: User, @PathVariable id : UUID): String {
        service.delete(id)
        appUtils.addBaseUrlAndUserNameToModel(model, user.name!!)
        addNotesToModel(model)
        return "book"
    }

    private fun addNotesToModel(model: Model) {
        val notes = service.getAll()
        model.addAttribute("notes", notes)
    }

    private fun addNotesToModel(model: Model, filter : String) {
        val filtered = service.search(filter)
        model.addAttribute("notes", filtered)
        model.addAttribute("filter", filter)
    }

    private fun addNoteToModel(model: Model, note : Note) {
        model.addAttribute("note", note)
    }
}