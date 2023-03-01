package ru.sber

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AddressBookApp

fun main(args: Array<String>) {
    runApplication<AddressBookApp>(*args)
}