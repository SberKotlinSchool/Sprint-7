package ru.sber.springdata.repositories

import ru.sber.springdata.entities.Appointment

interface CustomAppointmentRepository {
    fun findAllActualAppointment(): List<Appointment>
}