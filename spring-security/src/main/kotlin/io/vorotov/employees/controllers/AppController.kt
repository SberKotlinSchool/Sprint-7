package io.vorotov.employees.controllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import io.vorotov.employees.dto.HireEmployeeRq
import io.vorotov.employees.services.EmployeeService
import javax.validation.Valid

@Controller
@RequestMapping("app")
class AppController(private val employeeService: EmployeeService) {

    @PreAuthorize("hasRole('ADMIN') or hasRole('READ')")
    @GetMapping(path = ["", "list"])
    fun getEmployees(model: Model): String {
        model.addAttribute("employees", employeeService.getEmployees())
        return "list"
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('WRITE')")
    @GetMapping("add")
    fun showAdd(model: Model): String {
        model.addAttribute("employee", HireEmployeeRq())
        return "add"
    }

    @PostMapping("add")
    fun hireEmployee(@Valid @ModelAttribute("employee") employee: HireEmployeeRq, result: BindingResult): String {
        if (result.hasErrors()) {
            return "add"
        }
        employeeService.hireEmployee(employee)
        return "redirect:/app/list"
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('READ')")
    @GetMapping("{id}/view")
    fun getEmployees(@PathVariable("id") employeeId: Long, model: Model) =
        employeeService.getEmployees(employeeId).let { employees ->
            if (employees.isNotEmpty()) {
                model.addAttribute("employee", employees.first())
                "view"
            } else {
                "redirect:/app/list"
            }
        }

    @PreAuthorize("hasRole('ADMIN') or hasRole('WRITE')")
    @GetMapping("{id}/edit")
    fun editEmployee(@PathVariable("id") employeeId: Long, model: Model): String {
        val employees = employeeService.getEmployees(employeeId)
        return if (employees.isNotEmpty()) {
            model.addAttribute("employee", employees.first())
            "edit"
        } else {
            "redirect:/app/list"
        }
    }

    @PostMapping("{id}/edit")
    fun editEmployee(
        @PathVariable id: Long,
        @Valid @ModelAttribute("employee") employee: HireEmployeeRq,
        result: BindingResult
    ): String {
        if (result.hasErrors()) {
            return "edit"
        }
        employeeService.updateEmployee(id, employee)
        return "redirect:/app/list"
    }

    @GetMapping("{id}/delete")
    fun fireEmployee(@PathVariable("id") employeeId: Long, model: Model): String {
        employeeService.fireEmployee(employeeId)
        return "redirect:/app/list"
    }

}