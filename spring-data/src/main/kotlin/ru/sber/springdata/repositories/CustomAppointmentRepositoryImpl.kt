package ru.sber.springdata.repositories

import org.springframework.stereotype.Repository
import ru.sber.entities.Appointment
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class CustomAppointmentRepositoryImpl (
    @PersistenceContext
    private val entityManager: EntityManager
) : CustomAppointmentRepository {
    override fun findAllActualAppointment(): List<Appointment> =
        entityManager.createQuery("""from Appointment where dateEnd is null""").resultList as List<Appointment>
}