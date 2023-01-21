package ru.sber.springjpademo

import ru.sber.springjpademo.persistence.entities.*
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springjpademo.persistence.repository.*
import java.time.LocalDate

@SpringBootApplication
class SpringJpaDemoApplication(
    private val studentRepository: StudentRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) {

        //create
        val university = University(name = "MEI")
        val student1 = Student(
            name = "Petr",
            email = "petr@student.ru",
            studyType = StudyType.FULL_TIME,
            birthDate = LocalDate.now().minusYears(20),
            personalData = PersonalData("123", "74839"),
            homeAddress = HomeAddress(street = "Кутузовский пр-т", numberBuilding = 53),
            university = university,
            article = mutableListOf(Article(science = "A", publicistic = "B"), Article(science = "C", publicistic = "D")),
            inventory = mutableListOf(
                Inventory(inTheBag = "notebook", inThePocket = "Huba-Buba"),
                Inventory(inTheBag = "pen", inThePocket = "key")
            )
        )
        val student2 = Student(
            name = "Ivan",
            email = "ivan@student.ru",
            studyType = StudyType.PART_TIME,
            birthDate = LocalDate.now().minusYears(24),
            personalData = PersonalData("543", "341444"),
            homeAddress = HomeAddress(street = "Ленина", numberBuilding = 123),
            university = university,
            article = mutableListOf(Article(science = "A", publicistic = "B"), Article(science = "C", publicistic = "D")),
            inventory = mutableListOf(
                Inventory(inTheBag = "book", inThePocket = "ticket"),
                Inventory(inTheBag = "bottle", inThePocket = "card")
            )
        )
        val student3 = Student(
            name = "Anton",
            email = "anton@student.ru",
            studyType = StudyType.PART_TIME,
            birthDate = LocalDate.now().minusYears(18),
            personalData = PersonalData("436", "341456"),
            homeAddress = HomeAddress(street = "Комсомольская", numberBuilding = 1),
            university = university,
            article = mutableListOf(Article(science = "A", publicistic = "B"), Article(science = "C", publicistic = "D")),
            inventory = mutableListOf(
                Inventory(inTheBag = "keys", inThePocket = "dictionary"),
                Inventory(inTheBag = "bottle", inThePocket = "money")
            )
        )
        studentRepository.saveAll(listOf(student1,student2,student3))

        //read
        println(studentRepository.findAll())

        //update
        studentRepository.updateEmail(student1,"Bazinga@mail.com")
        println(studentRepository.findName("Petr"))

        //delete
        studentRepository.deleteStudent(student2)
        println(studentRepository.findAll())

    }
}

fun main(args: Array<String>) {
    runApplication<SpringJpaDemoApplication>(*args)
}
