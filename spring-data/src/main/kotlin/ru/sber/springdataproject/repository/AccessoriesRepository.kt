package ru.sber.springdataproject.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.springdataproject.entity.Accessory

interface AccessoriesRepository : JpaRepository<Accessory, Long> {
}