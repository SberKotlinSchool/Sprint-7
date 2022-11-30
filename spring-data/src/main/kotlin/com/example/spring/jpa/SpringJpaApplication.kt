package com.example.spring.jpa

import com.example.spring.jpa.entity.Address
import com.example.spring.jpa.entity.Book
import com.example.spring.jpa.entity.LibraryCard
import com.example.spring.jpa.repository.LibraryCardRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringJpaApplication(private val libraryCardRepository: LibraryCardRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val libraryCard1 = LibraryCard(
            lastName = "Иванов",
            firstName = "Иван",
            address = Address(street = "Ивановская"),
            books = mutableListOf(
                Book(title = "Жуга", author = "Дмитрий Скирюк"),
                Book(title = "Наёмник мёртвых богов", author = "Элеонора Раткевич")
            )
        )
        val libraryCard2 = LibraryCard(
            lastName = "Сидоров",
            firstName = "Александа",
            address = Address(street = "Вознесенская"),
            books = mutableListOf(Book(title = "Гроза", author = "Островский"))
        )

        libraryCardRepository.save(libraryCard1)

        libraryCardRepository.save(libraryCard2)

        println("=======================")
        val found = libraryCardRepository.findById(libraryCard1.id)
        println("Найден читательский билет: $found")
        println("=======================")

        val allLibraryCards = libraryCardRepository.findAll()
        println("Все читательские билеты: $allLibraryCards")
        println("=======================")

        libraryCard2.books.add(Book(title = "Идиот", author = "Достоевский"))
        libraryCardRepository.save(libraryCard2)
        val found2 = libraryCardRepository.findAllById(libraryCard2.id)
        println("Читательский билет обновлен: $found2")
        println("=======================")

        libraryCardRepository.delete(libraryCard2)
        val found3 = libraryCardRepository.findById(libraryCard2.id)
        println("Читательский билет удален: $found3")
        println("=======================")
    }

}

fun main(args: Array<String>) {
    runApplication<SpringJpaApplication>(*args)
}
