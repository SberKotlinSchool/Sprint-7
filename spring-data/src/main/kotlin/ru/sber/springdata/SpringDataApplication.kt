package ru.sber.springdata

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.domain.Pageable
import ru.sber.springdata.persistence.entity.BookEntity
import ru.sber.springdata.persistence.entity.HouseType
import ru.sber.springdata.persistence.entity.LibraryAddressEntity
import ru.sber.springdata.persistence.entity.LibraryEntity
import ru.sber.springdata.persistence.repository.BookRepository
import ru.sber.springdata.persistence.repository.LibraryRepository
import java.util.*
import kotlin.random.Random


@SpringBootApplication
class SpringDataApplication(
    private val libraryRepository: LibraryRepository,
    private val bookRepository: BookRepository,
) : CommandLineRunner {

  private val random: Random = Random.Default
  override fun run(vararg args: String?) {
    val library1 = LibraryEntity(
        address = LibraryAddressEntity(
            id = random.nextLong(),
            street = "Муром",
            houseType = HouseType.HIGH_RISE_BUILDING
        ),
        name = "тест",
        hasAbonementProgram = random.nextBoolean()
    ).let { libraryRepository.save(it) }

    val library2 = LibraryEntity(
        address = LibraryAddressEntity(
            id = random.nextLong(),
            street = "Москва",
            houseType = HouseType.HIGH_RISE_BUILDING
        ),
        name = "Библиотека",
        hasAbonementProgram = random.nextBoolean()
    ).let { libraryRepository.save(it) }

    println(library1)

    val initialBooks = List(10) {
      BookEntity(
          name = UUID.randomUUID().toString().take(5),
          isTaken = random.nextBoolean(),
          library = library1
      )
    }.map { book -> bookRepository.save(book) }

    val initialBooks2 = List(15) {
      BookEntity(
          name = UUID.randomUUID().toString().take(5),
          isTaken = random.nextBoolean(),
          library = library2
      )
    }.map { book -> bookRepository.save(book) }


    val foundBooks = bookRepository.findAllByLibrary(
        library1,
        Pageable.unpaged()
    )
    println(foundBooks.totalElements == initialBooks.size.toLong())

    bookRepository.deleteAllByLibraryId(library2.id)
    bookRepository.findAllByLibrary(
        library2,
        Pageable.unpaged()
    ).also {
      println(it.totalElements == 0L)
    }


  }
}

fun main(args: Array<String>) {
  runApplication<SpringDataApplication>(*args)
}