package ru.sber.app

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import ru.sber.domain.Record
import ru.sber.domain.RecordRepository

@Controller
@RequestMapping("/app")
class MvcController(val repository: RecordRepository) {

    @GetMapping("/list")
    fun getList(model: Model, @AuthenticationPrincipal user: User): String {
        if (user.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN")))
            model.addAttribute("records", repository.findAll())
        else
            model.addAttribute("records", repository.findAllByUsername(user.username))
        return "main"
    }

    @GetMapping("/search")
    fun getSearchList(@RequestParam query: String, model: Model, @AuthenticationPrincipal user: User): String {
        if (user.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN")))
            model.addAttribute("records", repository.findByNameContainingIgnoreCase(query))
        else
            model.addAttribute("records", repository.findByNameContainingIgnoreCase(query, user.username))
        return "main"
    }

    @PreAuthorize("hasRole('APP')")
    @GetMapping("/add")
    fun showAddForm(model: Model): String {
        val record = Record()
        model.addAttribute("record", record)
        return "add"
    }

    @PreAuthorize("hasRole('APP')")
    @PostMapping("/add")
    fun addRecord(@ModelAttribute record: Record, @AuthenticationPrincipal user: User, model: Model): String {
        record.username = user.username
        repository.save(record)
        return "redirect:/app/list"
    }

    @GetMapping("{id}/view")
    fun viewRecord(model: Model, @AuthenticationPrincipal user: User, @PathVariable id: Long): String {
        if (user.authorities.contains(SimpleGrantedAuthority("ROLE_ADMIN")))
            model.addAttribute("record", repository.findById(id).get())
        else
            model.addAttribute("record", repository.findByIdAndUsername(id, user.username))
        return "view"
    }

    @PreAuthorize("hasRole('APP')")
    @GetMapping("/{id}/edit")
    fun showEditForm(model: Model, @AuthenticationPrincipal user: User, @PathVariable id: Long): String {
        model.addAttribute("record", repository.findByIdAndUsername(id, user.username))
        return "edit"
    }

    @PreAuthorize("hasRole('APP')")
    @PostMapping("/{id}/edit")
    fun editRecord(@ModelAttribute record: Record, @AuthenticationPrincipal user: User, model: Model): String {
        record.username = user.username
        repository.save(record)
        return "redirect:/app/${record.id}/view"
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/delete")
    fun showDeleteForm(model: Model, @PathVariable id: Long): String {
        model.addAttribute("record", repository.findById(id).get())
        return "delete"
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/delete")
    fun deleteRecord(model: Model, @PathVariable id: Long): String {
        repository.deleteById(id)
        return "redirect:/app/list"
    }
}