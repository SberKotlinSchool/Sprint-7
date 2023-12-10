package com.example.demo.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class ControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `GET should return Entity with status 200 OK `() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/entity/1"))
        result
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json("{\"id\":1,\"name\":\"name of entity\"}"))
    }
}