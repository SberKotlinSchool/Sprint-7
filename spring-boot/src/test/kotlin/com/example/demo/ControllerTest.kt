package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
class ControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Get request to endpoint give_me_something should return content`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/give_me_something"))

        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("It is something from rest controller!"))
    }
}