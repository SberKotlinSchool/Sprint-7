package com.example

import com.example.model.Book
import com.example.model.Student
import com.example.repository.BookRepository
import com.example.repository.StudentRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication
class Application(
    private val bookRepository: BookRepository,
    private val studentRepository: StudentRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {

        val student = Student(
            name = "Student1",
            books = mutableListOf()
        )

        val book1 = Book(
            name = "Book1",
            student = student
        )

        val book2 = Book(
            name = "Book1",
            student = student
        )

        student.books = mutableListOf(book1, book2)

        bookRepository.save(book1)
        bookRepository.findById(book1.id)?.let { println("Book found by id: $it") }
        println("Find all student : ${studentRepository.findAll()}")

    }
}