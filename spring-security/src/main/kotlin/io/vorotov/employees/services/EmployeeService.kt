package io.vorotov.employees.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import io.vorotov.employees.dto.Employee
import io.vorotov.employees.dto.HireEmployeeRq

@Service
class EmployeeService {

    companion object {
        private val employeeBook = mutableMapOf<Long, Employee>()
        private var counter = 1L
        private val logger = LoggerFactory.getLogger(EmployeeService::class.java)
    }

    fun hireEmployee(employee: HireEmployeeRq): Employee {
        val newEmployee = Employee(counter, employee.fullName, employee.address, employee.phone, employee.email)
        employeeBook[counter] = newEmployee
        counter++
        logger.info("Создана запись в книге работников с id = ${newEmployee.id}")
        return newEmployee
    }

    fun getEmployees(employeeId: Long? = null): List<Employee?> {
        return if (employeeId == null) {
            logger.info("Получение всех записей в книге работников")
            employeeBook.values.toList()
        } else {
            logger.info("Получение из книги работников записи с id = $employeeId")
            employeeBook.filter { it.key == employeeId }.values.toList()
        }
    }

    fun updateEmployee(employeeId: Long, employee: HireEmployeeRq): Employee? {
        return if (employeeBook.containsKey(employeeId)) {
            logger.info("Обновлена запись в книге работников с id = $employeeId")
            val newEmployee =
                Employee(employeeId, employee.fullName, employee.address, employee.phone, employee.email)
            employeeBook[employeeId] = newEmployee
            newEmployee
        } else {
            logger.error("Не найдена запись в книге работников с id = $employeeId")
            null
        }
    }

    fun fireEmployee(employeeId: Long): Employee? {
        val result = employeeBook.remove(employeeId)
        if (result != null) {
            logger.info("Удалена запись в книге работников с id = $employeeId")
        } else {
            logger.error("Не найдена запись в книге работников с id = $employeeId")
        }
        return result
    }

}