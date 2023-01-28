package ru.sber.springdata.repository

import org.springframework.data.repository.CrudRepository
import ru.sber.springdata.entity.Passport

interface PassportRepository : CrudRepository<Passport, Long>