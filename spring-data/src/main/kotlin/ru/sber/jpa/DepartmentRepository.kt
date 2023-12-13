package ru.sber.jpa

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.entity.Department

interface DepartmentRepository : JpaRepository<Department, Long> {
}