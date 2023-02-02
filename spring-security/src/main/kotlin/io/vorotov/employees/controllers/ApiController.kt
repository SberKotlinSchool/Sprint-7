package io.vorotov.employees.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.vorotov.employees.dto.Employee
import io.vorotov.employees.dto.HireEmployeeRq
import io.vorotov.employees.dto.UpdateEmployeeRq
import io.vorotov.employees.services.EmployeeService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok

@RestController
@RequestMapping(path = ["/api"], produces = [APPLICATION_JSON_VALUE])
class ApiController(private val employeeService: EmployeeService) {

    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    fun hireEmployee(@RequestBody rq: HireEmployeeRq) = employeeService.hireEmployee(rq).let { e -> ok(e.id) }

    @GetMapping("{id}/view")
    fun getEmployee(@PathVariable("id") id: Long) = ok(employeeService.getEmployees(id))

    @GetMapping("view")
    fun getEmployees() = ok(employeeService.getEmployees())

    @PutMapping("{id}/edit")
    fun updateEmployee(@PathVariable("id") id: Long, @RequestBody rq: UpdateEmployeeRq): ResponseEntity<Employee> {
        val result = employeeService.updateEmployee(id, rq.address)
        return if (result != null) {
            ok(result)
        } else {
            notFound().build()
        }
    }

    @DeleteMapping("{id}/delete")
    fun fireEmployee(@PathVariable("id") id: Long): ResponseEntity<Unit> {
        val result = employeeService.fireEmployee(id)
        return if (result != null) {
            ok().build()
        } else {
            notFound().build()
        }
    }
}