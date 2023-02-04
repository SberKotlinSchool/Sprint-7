package ru.sber.springsecurity

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@SpringBootApplication
class SpringSecurityApplication

fun main(args: Array<String>) {
	runApplication<SpringSecurityApplication>(*args)
}
