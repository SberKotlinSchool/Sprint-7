package ru.sber.springdata.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.sber.entities.Appointment
import ru.sber.entities.Employee

@Repository
interface AppointmentRepository : CrudRepository<Appointment, Long> {
    //auto
    fun findByEmployee(employee: Employee): List<Appointment>

    //manual
    @Query("SELECT d FROM Appointment d " +
            "where d.dateEnd is null " +
            "and d.salary between :minSalary and :maxSalary")
    fun findActualAppointmentWithSalary(@Param("minSalary") minSalary: Double, @Param("maxSalary") maxSalary: Double): List<Appointment>
}
