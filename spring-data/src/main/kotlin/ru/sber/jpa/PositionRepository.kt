package ru.sber.jpa

import org.springframework.data.jpa.repository.JpaRepository
import ru.sber.entity.Position

interface PositionRepository : JpaRepository<Position, Long> {
}