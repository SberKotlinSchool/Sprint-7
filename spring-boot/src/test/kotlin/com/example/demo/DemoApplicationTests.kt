package com.example.demo

import com.example.demo.controller.Controller
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(Controller::class)
class DemoApplicationTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun getInfo() {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/test"))
            .andExpect(
                MockMvcResultMatchers.status().isOk
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.text").value("asdfasf"),
            )
            .andExpect(
            MockMvcResultMatchers.jsonPath("$.responseStatus").value("OK"),
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("$.responseStatus").value("OK"),
            )

    }
}
