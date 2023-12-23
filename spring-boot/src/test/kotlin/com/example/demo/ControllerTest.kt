package com.example.demo

import com.example.demo.controller.Controller
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
@WebMvcTest(Controller::class)
class ControllerTest {

    @Autowired private lateinit var mockMVC: MockMvc

    @Test
    fun `sayHello function test`(){

        mockMVC.perform(get(("/hello")))
            .andExpect(status().is2xxSuccessful)
            .andExpect(content().string("Hello!"))


    }
}