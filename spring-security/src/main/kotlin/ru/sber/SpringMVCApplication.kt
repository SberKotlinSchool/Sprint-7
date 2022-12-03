package ru.sber

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
@EnableWebMvc
class SpringMVCApplication

fun main(args: Array<String>) {
	runApplication<SpringMVCApplication>(*args)
}
