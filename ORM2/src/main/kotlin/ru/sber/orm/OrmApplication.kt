package ru.sber.orm

import org.hibernate.cfg.Configuration
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.orm.entity.BookEntity
import ru.sber.orm.entity.HouseType
import ru.sber.orm.entity.LibraryAddressEntity
import ru.sber.orm.entity.LibraryEntity
import ru.sber.orm.repository.BookRepository
import ru.sber.orm.repository.LibraryRepository
import java.util.*
import kotlin.random.Random

@SpringBootApplication
class OrmApplication(
) : CommandLineRunner {
	override fun run(vararg args: String?) {
		println("started")
		val sf = Configuration().configure()
				.addAnnotatedClass(LibraryEntity::class.java)
				.addAnnotatedClass(LibraryAddressEntity::class.java)
				.addAnnotatedClass(BookEntity::class.java)
				.buildSessionFactory()
		sf.use {
			val libraryRepository = LibraryRepository(it)
			val bookRepository = BookRepository(it)
			val random = Random.Default

			val library1 = LibraryEntity(
					address = LibraryAddressEntity(
							id = 1,
							street = "муром",
							houseType = HouseType.HIGH_RISE_BUILDING
					),
					name = "Тест",
					hasAbonementProgram = random.nextBoolean()
			)

			libraryRepository.save(library1)

			val library2 = LibraryEntity(
					address = LibraryAddressEntity(
							id = 2,
							street = "москва",
							houseType = HouseType.DETACHED_HOUSE
					),
					name = "Библиотека",
					hasAbonementProgram = random.nextBoolean()
			)


			libraryRepository.save(library2)

			val initialBooks1 = List(10) {
				BookEntity(
						name = UUID.randomUUID().toString().take(10),
						isTaken = random.nextBoolean(),
						library = library1
				)
			}.onEach { book -> bookRepository.save(book) }

			val initialBooks2 = List(15) {
				BookEntity(
						name = UUID.randomUUID().toString().take(10),
						isTaken = random.nextBoolean(),
						library = library2
				)
			}.onEach { book -> bookRepository.save(book) }

			val found1 = libraryRepository.find(library1.id)
			val found2 = libraryRepository.find(library1.name)
			println(found1)
			println(found2)
			println("found1 == found2: ${found1?.id == found2?.id}")

			val books = bookRepository.findByLibrary(library2).onEach { println(it) }
			println(initialBooks1.size == books.size)

			val allBooks = bookRepository.getAll().onEach { println(it) }


		}
	}


}

fun main(args: Array<String>) {
	runApplication<OrmApplication>(*args)
}
