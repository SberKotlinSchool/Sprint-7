package ru.sber.springdataproject.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.RepositoryDefinition
import org.springframework.stereotype.Repository
import ru.sber.springdataproject.entity.Accessory

@Repository
interface AccessoriesRepository : JpaRepository<Accessory, Long> {
}