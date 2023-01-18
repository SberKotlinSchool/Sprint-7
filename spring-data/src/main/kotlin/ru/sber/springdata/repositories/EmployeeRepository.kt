package ru.sber.springdata.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.springdata.entities.Employee

@Repository
interface EmployeeRepository : CrudRepository<Employee, Long>