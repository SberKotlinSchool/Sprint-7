package ru.sber.jpa

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.entity.Employee

interface EmployeeRepository : JpaRepository<Employee, Long> {
}