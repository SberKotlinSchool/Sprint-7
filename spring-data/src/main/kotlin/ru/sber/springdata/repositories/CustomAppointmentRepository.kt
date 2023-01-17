package ru.sber.springdata.repositories

import ru.sber.entities.Appointment

interface CustomAppointmentRepository {
    fun findAllActualAppointment(): List<Appointment>
}