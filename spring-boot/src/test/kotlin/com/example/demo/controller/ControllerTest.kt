package com.example.demo.controller

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(Controller::class)
class ControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun foo() {
        mockMvc.perform(get("/api/hello"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().string("Hello, Spring!"))
    }
}