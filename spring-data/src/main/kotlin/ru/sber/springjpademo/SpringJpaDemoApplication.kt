package ru.sber.springjpademo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springjpademo.persistence.entity.*
import ru.sber.springjpademo.persistence.repository.StudentRepository
import java.time.LocalDate
import kotlin.random.Random

@SpringBootApplication
class SpringJpaDemoApplication(
    private val studentRepository: StudentRepository,
) : CommandLineRunner {

    override fun run(vararg args: String?) {

        studentRepository.deleteAll()

        val student1 = Student(
            name = "Petr",
            email = "petr@student.ru",
            studyType = StudyType.FULL_TIME,
            birthDate = LocalDate.now().minusYears(Random.nextLong(20, 25)),
            personalData = PersonalData(Random.nextInt(100, 999).toString(), Random.nextInt(100000, 999999).toString()),
            homeAddress = listOf(HomeAddress(street = "Кутузовский пр-т"), HomeAddress(street = "Южнопортовый"))
        )
        val student2 = Student(
            name = "Ivan",
            email = "ivan@student.ru",
            studyType = StudyType.PART_TIME,
            birthDate = LocalDate.now().minusYears(Random.nextLong(20, 25)),
            personalData = PersonalData(Random.nextInt(100, 999).toString(), Random.nextInt(100000, 999999).toString()),
            homeAddress = listOf(HomeAddress(street = "Ленина"))
        )

        studentRepository.saveAll(listOf(student1, student2))

        studentRepository.findById(student1.id)
            .map { student -> println("Найден студент по id: $student") }
            .orElseGet { println("Студент не найден по id = ${student1.id}") }


        studentRepository.findByEmail(student2.email)
            .map { student -> println("Найден студент по email: $student") }
            .orElseGet { println("Студент не найден по email = ${student2.email}") }

        val resultAll = studentRepository.findAll()
        println("Все студенты $resultAll")
    }
}

fun main(args: Array<String>) {
    runApplication<SpringJpaDemoApplication>(*args)
}
