package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `any Get rq should return array of entities with 200 code`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/"))

        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$[0].id").isNumber)
            .andExpect(jsonPath("$[0].name").isString)
    }
}