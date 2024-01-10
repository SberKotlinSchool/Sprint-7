package com.example.demo.controller

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.hamcrest.Matchers.containsString
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content

@WebMvcTest(Controller::class)
class ControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun test() {
        mockMvc.perform(get("/test"))
            .andExpect(status().isOk)
            .andExpect { content().string(containsString("OK")) }
    }
}