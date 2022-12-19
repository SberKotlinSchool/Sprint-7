package sber
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import sber.enteties.Cat
import sber.enteties.CatClass
import sber.repositories.CatRepository
import java.time.LocalDate

@SpringBootApplication
class SpringJpaApplication(private val catRepository: CatRepository) {

}

fun main(args: Array<String>) {
    runApplication<SpringJpaApplication>(*args)
}