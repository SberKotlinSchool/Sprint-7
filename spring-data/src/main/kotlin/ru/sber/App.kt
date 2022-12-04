package ru.sber

import ru.sber.enteties.Address
import ru.sber.enteties.Student
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.repository.StudentRepository
import ru.sber.enteties.Major

@SpringBootApplication
class App(private val studentRepository: StudentRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {

        val student1 = Student(
            lastName = "Иванов",
            firstName = "Валерий",
            address = Address(city = "Санкт-Петербург", street = "Невский"),
            major = mutableListOf(
                Major(code = "117054", name = "Ядерная физика"),
                Major(code = "234508", name = "Рисование")
            )
        )
        val student2 = Student(
            lastName = "Петров",
            firstName = "Александр",
            address = Address(city = "Санкт-Петербург", street = "Рубинштейна"),
            major = mutableListOf(
                Major(code = "317054", name = "Древняя литература")
            )
        )

        studentRepository.save(student1)
        studentRepository.save(student2)

        val found = studentRepository.findById(student1.id)
        println("Найден студент: $found \n")

        val allLibraryCards = studentRepository.findAll()
        println("Все студенты: $allLibraryCards")

        student2.major.add(Major(code = "456009", name = "ОБЖ"))
        studentRepository.save(student2)
        val found2 = studentRepository.findAllById(student2.id)
        println("Информация о студенте обновлена: $found2")

        studentRepository.delete(student2)
        val found3 = studentRepository.findAllById(student2.id)
        println("Студент удален: $found3")

    }
}

fun main(args: Array<String>) {
    runApplication<App>(*args)
}