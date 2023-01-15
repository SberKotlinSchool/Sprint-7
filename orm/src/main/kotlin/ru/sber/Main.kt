package ru.sber

import org.hibernate.cfg.Configuration
import ru.sber.dao.AppointmentDAO
import ru.sber.dao.DepartmentDAO
import ru.sber.dao.EmployeeDAO
import ru.sber.dao.PositionDAO
import ru.sber.entities.*
import java.time.LocalDate
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Employee::class.java)
        .addAnnotatedClass(Department::class.java)
        .addAnnotatedClass(Position::class.java)
        .addAnnotatedClass(Appointment::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val appointmentDAO = AppointmentDAO(sessionFactory)
        val employeeDAO = EmployeeDAO(sessionFactory)
        val departmentDAO = DepartmentDAO(sessionFactory)
        val positionDAO = PositionDAO(sessionFactory)

        val employee1 = Employee(
            lastName = "Иванов",
            firstName = "Иван",
            middleName = "Петрович",
            phoneNumber = "89088001111",
            email = "ivanov2@employee.ru",
            birthDate = LocalDate.now().minusYears(20),
            workingMode = WorkingMode.OFFICE_MODE,
            personalData = PersonalData("1234 111111", "74839"),
        )
        val employee2 = Employee(
            lastName = "Сергеев",
            firstName = "Александр",
            middleName = "Иванович",
            phoneNumber = "89083333333",
            email = "sergeev2@employee.ru",
            birthDate = LocalDate.now().minusYears(30),
            workingMode = WorkingMode.REMOTE_MODE,
            personalData = PersonalData("3333 123456", "10407"),
        )

        employeeDAO.save(employee1)
        employeeDAO.save(employee2)

        val department = Department(
            name = "Отдел тестирования ПО",
            chief = employee2
        )

        departmentDAO.save(department)

        val post1 = Position(
            name = "Аналитик",
            department = department,
            minSalary = 50000.00,
            maxSalary = 100000.00
        )
        val post2 = Position(
            name = "Тестировщик",
            department = department,
            minSalary = 40000.00,
            maxSalary = 700000.00
        )
        val post3 = Position(
            name = "Начальник отдела",
            department = department,
            minSalary = 100000.00,
            maxSalary = 150000.00
        )

        positionDAO.save(post1)
        positionDAO.save(post2)
        positionDAO.save(post3)

        val appointment1 = Appointment(
            employee = employee1,
            post = post2,
            salary = 45000.00,
            dateBegin = LocalDate.of(2022, 9, 1),
            dateEnd = LocalDate.of(2022, 12, 31)
        )
        val appointment2 = Appointment(
            employee = employee1,
            post = post1,
            salary = 65000.00,
            dateBegin = LocalDate.of(2023, 1, 1)
        )
        val appointment3 = Appointment(
            employee = employee2,
            post = post3,
            salary = 110000.00,
            dateBegin = LocalDate.of(2022, 1, 1)
        )

        appointmentDAO.save(appointment1)
        appointmentDAO.save(appointment2)
        appointmentDAO.save(appointment3)

        var found = appointmentDAO.findByEmployee(employee1)
        println("Найдно назначение: $found \n")

        val allAppointments = appointmentDAO.findAll()
        println("Все назначения: $allAppointments")
    }
}