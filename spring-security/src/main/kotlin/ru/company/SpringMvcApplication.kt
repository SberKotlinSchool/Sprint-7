package ru.company

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc


@SpringBootApplication
@EnableWebMvc
class SpringMvcApplication

fun main(args: Array<String>) {
    runApplication<SpringMvcApplication>(*args)
}
