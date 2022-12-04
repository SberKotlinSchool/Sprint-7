package ru.sber.study.kotlin

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.study.kotlin.orm.persistence.entities.*
import ru.sber.study.kotlin.orm.persistence.repository.DeveloperRepository
import ru.sber.study.kotlin.orm.persistence.repository.ItemRepository
import ru.sber.study.kotlin.orm.persistence.repository.PerformerRepository

@SpringBootApplication
class SpringApplication(
    private val developerRepository: DeveloperRepository,
    private val performerRepository: PerformerRepository,
    private val itemRepository: ItemRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val developerName1 = "Deutsche Grammophon"
        val developerName2 = "Polydor"

        val developer1 = developerRepository.findByName(developerName1) ?: Developer(name = developerName1)
            .let { developerRepository.save(it) }

        val developer2 = developerRepository.findByName(developerName2) ?: Developer(name = developerName2)
            .let { developerRepository.save(it) }


        val performerName1 = "John Williams"
        val performerName2 = "Berliner Philharmoniker"
        val performer1 = performerRepository.findByName(performerName1) ?: Performer(name = performerName1)
            .let { performerRepository.save(it) }

        val performer2 = performerRepository.findByName(performerName2) ?: Performer(name = performerName2)
            .let { performerRepository.save(it) }

        Item(
            type = ItemType.CD,
            identity = ItemIdentity(developer = developer1, catalogNumber = "00001"),
            name = "Music from movies",
            performers = listOf(performer1, performer2)
        )
            .let { itemRepository.save(it) }

        Item(
            type = ItemType.LP,
            identity = ItemIdentity(developer = developer2, catalogNumber = "00010"),
            name = "Beethoven. Symphony No. 5",
            performers = listOf(performer2)
        )
            .let { itemRepository.save(it) }

        val items = itemRepository.findAll()
        println(items)
    }
}

fun main(args: Array<String>) {
    runApplication<SpringApplication>(*args)
}