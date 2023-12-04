package com.example.demo.controller

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(StudentController::class)
class StudentControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `GET student should return content successfully with status 200 OK`() {
        mockMvc.perform(MockMvcRequestBuilders.get(("/api/students")))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("""[{"id":1,"name":"Петя","group":"1пр"},{"id":2,"name":"Вася","group":"3пр"}]"""))
    }
}