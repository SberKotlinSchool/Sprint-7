package ru.sber.jpademo

import ru.sber.jpademo.entity.History
import ru.sber.jpademo.entity.UserChecker
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.jpademo.repository.HistoryRepository
import ru.sber.jpademo.repository.UserCheckerRepository

@SpringBootApplication
class SpringJpaDemoApplication(
    private val userCheckerRepository: UserCheckerRepository,
    private val historyRepository: HistoryRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val userChecker = UserChecker(
            name = "Ivan", email = "ivan@mail.ru"
        )
        userCheckerRepository.save(userChecker)
        val userFound = userCheckerRepository.findById(userChecker.id)
        println("Найден пользователь: $userFound \n")
        val allUsers = userCheckerRepository.findAll()
        println("Все пользователи: $allUsers")

        val history = History(
            number = 123, result = false
        )
        historyRepository.save(history)
        val historyFound = historyRepository.findByNumber(history.number)
        println("Найдена история проверки: $historyFound \n")
    }
}

fun main(args: Array<String>) {
    runApplication<SpringJpaDemoApplication>(*args)
}
