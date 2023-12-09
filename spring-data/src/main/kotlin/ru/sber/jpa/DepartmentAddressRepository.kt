package ru.sber.jpa

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.entity.DepartmentAddress

interface DepartmentAddressRepository : JpaRepository<DepartmentAddress, Long> {
}