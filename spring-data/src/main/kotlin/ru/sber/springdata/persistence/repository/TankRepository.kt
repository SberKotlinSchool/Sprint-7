package ru.sber.springdata.persistence.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.springdata.persistence.entity.Tank

@Repository
interface TankRepository : CrudRepository<Tank, Long> {

}