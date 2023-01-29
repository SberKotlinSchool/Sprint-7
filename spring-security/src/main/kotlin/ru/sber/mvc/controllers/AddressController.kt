package ru.sber.mvc.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import ru.sber.mvc.models.Address
import ru.sber.mvc.repositories.AddressBookRepo

@Controller
@RequestMapping("/app")
class AddressController(@Autowired val repository: AddressBookRepo) {

    //  /app/list просмотр записей и поиск если будет переданы query параметры запроса
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    //@GetMapping(path = ["", "list"])
    @GetMapping("/list")
    fun list(@RequestParam(required = false) name: String?, @RequestParam(required = false) phone: String?, model: Model): String {
        model.addAttribute("rows", repository.getList(name, phone))
        return "list"
    }

    // /app/{id}/view просмотр конкретной записи
    @PreAuthorize("hasRole('ADMIN') or hasRole('READER')")
    @GetMapping("/{id}/view")
    fun view(model: Model, @PathVariable id: String): String {
        model.addAttribute("row", repository.getById(id.toInt()))
        return "view"
    }

    // /app/{id}/edit редактирование конкретной записи
    @PreAuthorize("hasRole('ADMIN') or hasRole('WRITER')")
    @GetMapping("/{id}/edit")
    fun editGet(model: Model, @PathVariable id: String): String {
        model.addAttribute("row", repository.getById(id.toInt()))
        return "edit"
    }

    @PostMapping("/{id}/edit")
    fun editPost(row: Address, @PathVariable id: String, result: BindingResult): String {
        if (result.hasErrors()) {
            return "edit"
        }
        repository.update(row)
        return "redirect:/app/list"
    }

    // /app/add добавление записи
    @PreAuthorize("hasRole('ADMIN') or hasRole('WRITER')")
    @GetMapping("/add")
    fun add(model: Model) :String{
        model.addAttribute("row", Address())
        return "add"
    }

    @PostMapping("/add")
    fun add(row: Address, result: BindingResult) :String {
        if (result.hasErrors()) {
            return "add"
        }
        repository.insert(row)
        return "redirect:/app/list"
    }

    // /app/{id}/delete удаление конкретной записи
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/delete")
    fun delete(@PathVariable id: String):String {
        repository.delete(id.toInt())
        return "redirect:/app/list"
    }

}