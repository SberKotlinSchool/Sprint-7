package com.example.demo

import com.example.demo.controller.Controller
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(Controller::class)
class WebApplicationTest {

    @Autowired
    private lateinit var mock: MockMvc

    @Test
    fun `get query should return Ok`() {

        mock.perform(
            MockMvcRequestBuilders
            .get(("/test")))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("test"))
    }
}