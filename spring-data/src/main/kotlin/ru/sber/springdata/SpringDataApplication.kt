package ru.sber.springdata

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springdata.entities.Article
import ru.sber.springdata.entities.Author
import ru.sber.springdata.entities.AuthorContacts
import ru.sber.springdata.entities.Magazine
import ru.sber.springdata.repositories.CustomMagazinesRepository
import ru.sber.springdata.repositories.MagazinesRepository

@SpringBootApplication
class SpringDataApplication(
    private val magazinesRepository: MagazinesRepository,
    private val customMagazinesRepository: CustomMagazinesRepository
): CommandLineRunner {

    override fun run(vararg args: String?) {
        val m = Magazine(
            name = "mens health2",
            articles = mutableSetOf(
                Article(
                    title = "Hey",
                    contens = "Being friendly is awesome!!",
                    authors = mutableSetOf(
                        Author(
                            name = "Dmitriy",
                            surname = "Smirnov",
                            contacts = AuthorContacts(phone = "1234", email = "mail@mail.com")
                        ),
                        Author(
                            name = "Nikolay",
                            surname = "Sharonov",
                            contacts = AuthorContacts(phone = "+79998765432", email = "nikmail@mail.com")
                        )
                    )
                )
            )
        )

        magazinesRepository.save(m)


        customMagazinesRepository.getAllMagazine().forEach {
            println(it.id)
            println(it.name)
        }
    }
}

fun main(args: Array<String>) {
    runApplication<SpringDataApplication>(*args)
}
