package ru.sber.springdata

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.orm.entities.Student
import ru.sber.orm.entities.Subject
import ru.sber.orm.entities.Teacher
import ru.sber.springdata.repository.SubjectRepository

@SpringBootApplication
class SubjectApplication(
    private val subjectRepository: SubjectRepository
): CommandLineRunner {

    override fun run(vararg args: String?) {

//        val teacher1 = Teacher(name = "teacher1 teacherovich", age = 50)
//        val teacher2 = Teacher(name = "teacher2 teacherovich", age = 65)
//        val teacher3 = Teacher(name = "teacher3 teacherovich", age = 54)
//
//        val student1 = Student(name = "student1", email = "student1@mail.ru")
//        val student2 = Student(name = "student2", email = "student2@mail.ru")
//        val student3 = Student(name = "student3", email = "student3@mail.ru")
//        val student4 = Student(name = "student4", email = "student4@mail.ru")
//
//        val subject1 = Subject(
//            name = "subject1",
//            teacher = teacher1,
//            students = mutableListOf(student1, student2)
//        )
//
//        val subject2 = Subject(
//            name = "subject2",
//            teacher = teacher2,
//            students = mutableListOf(student2, student3)
//        )
//
//        val subject3 = Subject(
//            name = "subject3",
//            teacher = teacher3,
//            students = mutableListOf(student1, student2, student4)
//        )
//
//        val subject4 = Subject(
//            name = "subject4",
//            teacher = teacher1,
//            students = mutableListOf(student1, student2, student3, student4)
//        )

       println(subjectRepository.findAll())

    }
}

fun main(args: Array<String>) {
    runApplication<SubjectApplication>(*args)
}

