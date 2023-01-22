package ru.sbrf.addressbook.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import ru.sbrf.addressbook.core.AddressBookService
import ru.sbrf.addressbook.core.Employee
import ru.sbrf.addressbook.core.UserDetailsAdapter
import javax.validation.Valid


@Controller
class IndexController() {

    @GetMapping("/")
    fun index(): String = "redirect:/app/list"
}

@Controller
@RequestMapping("/app")
class AddressBookMvcController @Autowired constructor(val service: AddressBookService) {

    @GetMapping("/")
    fun index(): String = "redirect:/app/list"

    @GetMapping(value = ["/add"])
    fun addEmployee(model: Model): String {
        model.addAttribute("employee", Employee())
        return "add"
    }

    @PostMapping(value = ["/add"])
    fun addEmployee(
        @Valid @ModelAttribute("employee") employee: Employee,
        auth: Authentication,
        result: BindingResult
    ): String {
        if (result.hasErrors()) {
            return "add"
        }
        val user = (auth.principal as UserDetailsAdapter).user
        if (employee.owner == null) {
            employee.owner = user.id
        }
        service.addEmployee(employee, user.username)
        return "redirect:/app/list"
    }


    @GetMapping(value = ["/list"])
    fun listEmployees(
        @RequestParam(required = false) firstname: String?,
        @RequestParam(required = false) lastname: String?,
        @RequestParam(required = false) phone: String?,
        @RequestParam(required = false) email: String?,
        model: Model
    ): String {
        model.addAttribute(
            "employees",
            service.getEmployees(firstname, lastname, phone, email)
        )
        return "list"
    }


    @PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'ru.sbrf.addressbook.core.Employee','DELETE') or hasPermission(#id, 'ru.sbrf.addressbook.core.Employee','READ')")
    @GetMapping(value = ["/{id}/view"])
    fun viewEmployee(@PathVariable id: Long, model: Model): String {
        model.addAttribute("employee", service.getEmployeeById(id))
        return "view"
    }

    @PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'ru.sbrf.addressbook.core.Employee','DELETE')")
    @GetMapping(value = ["/{id}/edit"])
    fun editEmployee(@PathVariable id: Long, model: Model): String {
        model.addAttribute("employee", service.getEmployeeById(id))
        return "edit"
    }

    @PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'ru.sbrf.addressbook.core.Employee','DELETE')")
    @PostMapping(value = ["/{id}/edit"])
    fun editEmployee(
        @PathVariable id: Int,
        @Valid @ModelAttribute("employee") employee: Employee,
        result: BindingResult
    ): String {
        if (result.hasErrors()) {
            return "edit"
        }
        service.updateEmployee(employee)
        return "redirect:/app/list"
    }

    @PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'ru.sbrf.addressbook.core.Employee','DELETE')")
    @GetMapping("/{id}/delete")
    fun deleteEmployee(@PathVariable id: Long, model: Model): String {
        service.removeEmployeeById(id)
        return "redirect:/app/list"
    }


}

@RestController
@RequestMapping("/api")
class AddressBookRestController @Autowired constructor(val service: AddressBookService) {

    @PostMapping(value = ["/add"])
    fun addEmployee(@Valid @RequestBody employee: Employee, auth: Authentication): ResponseEntity<Employee> {
        val user = (auth.principal as UserDetailsAdapter).user
        service.addEmployee(employee, user.username)
        return ResponseEntity.ok(employee)
    }

    @GetMapping(value = ["/list"])
    fun listEmployees(
        @RequestParam(required = false) firstname: String?,
        @RequestParam(required = false) lastname: String?,
        @RequestParam(required = false) phone: String?,
        @RequestParam(required = false) email: String?
    ): ResponseEntity<List<Employee>> {
        return ResponseEntity.ok(service.getEmployees(firstname, lastname, phone, email))
    }

    @GetMapping(value = ["/{id}/view"])
    fun viewEmployee(@PathVariable id: Long): ResponseEntity<Employee?> {
        return ResponseEntity.ok(service.getEmployeeById(id))
    }

    @PutMapping(value = ["/{id}/edit"])
    fun editEmployee(
        @PathVariable id: Int,
        @Valid @RequestBody employee: Employee,
        result: BindingResult
    ): ResponseEntity<Employee?> {
        service.updateEmployee(employee)
        return ResponseEntity.ok(employee)
    }

    @DeleteMapping(value = ["/{id}/delete"])
    fun deleteEmployee(@PathVariable id: Long) {
        service.removeEmployeeById(id)
    }
}