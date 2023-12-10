package ru.sber.springsec

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringSecApp

fun main(args: Array<String>) {
    runApplication<SpringSecApp>(*args)
}