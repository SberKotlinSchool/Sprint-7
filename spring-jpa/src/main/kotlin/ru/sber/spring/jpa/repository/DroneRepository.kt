package ru.sber.spring.jpa.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sber.spring.jpa.entity.Drone


@Repository
interface DroneRepository: JpaRepository<Drone, Long> {

}