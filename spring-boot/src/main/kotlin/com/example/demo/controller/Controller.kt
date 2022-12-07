package com.example.demo.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api")
class Controller {

    @GetMapping("/test", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAnything(): AnythingResponseBody =
        AnythingResponseBody(
            responseStatus = "OK",
            text = "asdfasf"
        )
}


data class AnythingResponseBody(
    val dateTime: String = LocalDate.now().toString(),
    val responseStatus: String,
    val text: String
)