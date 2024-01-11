package ru.sber.springdata.repository

import ru.sber.springdata.entity.Citizen
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CitizenRepository : CrudRepository<Citizen, Int>