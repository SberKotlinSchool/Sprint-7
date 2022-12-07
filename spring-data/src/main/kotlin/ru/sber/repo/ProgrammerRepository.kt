package ru.sber.repo

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.entities.Programmer

@Repository
interface ProgrammerRepository : CrudRepository<Programmer, Long> {
}