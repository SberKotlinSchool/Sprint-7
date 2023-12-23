package ru.sber.springjpademo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.sber.springjpademo.persistence.repository.SubjectRepository

@SpringBootApplication
class SpringJpaDemoApplication(
    private val subjectRepository: SubjectRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {


        val resultAll = subjectRepository.findAll()
        println(resultAll)
    }
}

fun main(args: Array<String>) {
    runApplication<SpringJpaDemoApplication>(*args)
}
