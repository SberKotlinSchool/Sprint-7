package com.example.adresbook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@SpringBootApplication
@ServletComponentScan
class AddressBookSecurityApplication

fun main(args: Array<String>) {
    runApplication<AddressBookSecurityApplication>(*args)
}