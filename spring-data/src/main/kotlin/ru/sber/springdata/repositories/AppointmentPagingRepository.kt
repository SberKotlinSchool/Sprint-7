package ru.sber.springdata.repositories

import org.springframework.data.repository.PagingAndSortingRepository
import ru.sber.springdata.entities.Appointment

interface AppointmentPagingRepository : PagingAndSortingRepository<Appointment, Long>