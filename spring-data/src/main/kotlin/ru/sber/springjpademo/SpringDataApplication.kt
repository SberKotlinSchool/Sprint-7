package ru.sber.springjpademo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import ru.sber.springjpademo.persistence.entity.BookEntity
import ru.sber.springjpademo.persistence.entity.HouseType
import ru.sber.springjpademo.persistence.entity.LibraryAddressEntity
import ru.sber.springjpademo.persistence.entity.LibraryEntity
import ru.sber.springjpademo.persistence.repository.BookRepository
import ru.sber.springjpademo.persistence.repository.LibraryRepository
import java.util.*
import kotlin.random.Random

@SpringBootApplication
class SpringDataApplication(
    private val libraryRepository: LibraryRepository,
    private val bookRepository: BookRepository,
) : CommandLineRunner {

    private val random: Random = Random.Default

    override fun run(vararg args: String?) {

        val library = LibraryEntity(
            address = LibraryAddressEntity(
                street = "Кутузовский пр-т",
                houseType = HouseType.HIGH_RISE_BUILDING
            ),
            name = "Библиотека №1",
            hasAbonementProgram = random.nextBoolean()
        ).let { libraryRepository.save(it) }

        println(library)

        val initialBooks = List(10) {
            BookEntity(
                name = UUID.randomUUID().toString().take(5),
                isTaken = random.nextBoolean(),
                library = library
            )
        }.map { book -> bookRepository.save(book) }

        val found1 = libraryRepository.findByName(library.name)
        val found2 = libraryRepository.findById(library.id).orElseGet { null }
        println(found1?.id != found2?.id)

        val foundBooks = bookRepository.findAllByLibrary(
            library,
            PageRequest.of(0, 2, Sort.by("id"))
        )
        println(foundBooks.totalElements == initialBooks.size.toLong())

        bookRepository.deleteAllByLibraryId(library.id)
        bookRepository.findAllByLibrary(
            library,
            PageRequest.ofSize(2)
        ).also {
            println(it.totalElements != 0L)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<SpringDataApplication>(*args)
}
