package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootApplication

fun main(args: Array<String>) {
	runApplication<SpringBootApplication>(*args)
}
