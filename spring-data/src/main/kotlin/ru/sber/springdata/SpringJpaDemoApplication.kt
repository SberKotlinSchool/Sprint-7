package ru.sber.springdata

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import ru.sber.entities.*
import ru.sber.springdata.repositories.*
import java.time.LocalDate

@SpringBootApplication
@EntityScan("ru.sber.entities")
@EnableJpaRepositories("ru.sber.springdata.repositories")
class SpringJpaDemoApplication (
    private val employeeRepository: EmployeeRepository,
    private val departmentRepository: DepartmentRepository,
    private val positionRepository: PositionRepository,
    private val appointmentRepository: AppointmentRepository,
    private val appointmentPagingRepository: AppointmentPagingRepository,
    private val customAppointmentRepository: CustomAppointmentRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val employee1 = Employee(
            lastName = "Иванов",
            firstName = "Иван",
            middleName = "Петрович",
            phoneNumber = "89088001111",
            email = "ivanov12@employee.ru",
            birthDate = LocalDate.now().minusYears(20),
            workingMode = WorkingMode.OFFICE_MODE,
            personalData = PersonalData("1234 111111", "74839"),
        )
        val employee2 = Employee(
            lastName = "Сергеев",
            firstName = "Александр",
            middleName = "Иванович",
            phoneNumber = "89083333333",
            email = "sergeev12@employee.ru",
            birthDate = LocalDate.now().minusYears(30),
            workingMode = WorkingMode.REMOTE_MODE,
            personalData = PersonalData("3333 123456", "10407"),
        )
        employeeRepository.saveAll(listOf(employee1, employee2))

        val department = Department(
            name = "Отдел тестирования ПО",
            chief = employee2
        )
        departmentRepository.save(department)

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
        positionRepository.saveAll(listOf(post1, post2, post3))

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
        appointmentRepository.saveAll(listOf(appointment1, appointment2, appointment3))

        val appointments = appointmentRepository.findByEmployee(employee1)
        val appointmentsWithSalary = appointmentRepository.findActualAppointmentWithSalary(45000.00, 65000.00)
        println(appointments)
        println(appointmentsWithSalary)

        val pageResult = appointmentPagingRepository.findAll(PageRequest.of(1, 10))
        println("Total elements: ${pageResult.totalElements}")
        println("Total pages: ${pageResult.totalPages}")
        println("page 1: ${pageResult.content}")

        val resultAll = customAppointmentRepository.findAllActualAppointment()
        println(resultAll)
    }
}

fun main(args: Array<String>) {
    runApplication<SpringJpaDemoApplication>(*args)
}