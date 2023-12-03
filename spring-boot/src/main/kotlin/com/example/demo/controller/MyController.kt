package com.example.demo.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MyController {

    @GetMapping("/hello")
    fun hello(): RestResponse<String> {
        return RestResponse(true, "data ok")

    }
}