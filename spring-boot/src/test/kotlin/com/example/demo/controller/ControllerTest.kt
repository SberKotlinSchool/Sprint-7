package com.example.demo.controller.controller

import com.example.demo.controller.Controller
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(Controller::class)
internal class ControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testMainPage() {
        val page = mockMvc.perform(MockMvcRequestBuilders.get("/"))

        page
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("Main page"))
    }
}