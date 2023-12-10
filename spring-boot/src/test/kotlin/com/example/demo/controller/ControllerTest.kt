package com.example.demo.controller

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(Controller::class)
class ControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun helloTest() {
        mockMvc.perform(get("/hello"))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().string("Hello!"))
    }
}