package ru.sber.springdata.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sber.springdata.entities.Position

@Repository
interface PositionRepository : CrudRepository<Position, Long>