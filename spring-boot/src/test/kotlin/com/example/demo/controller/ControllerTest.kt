package com.example.demo.controller

import com.example.demo.persistance.Entity
import org.hamcrest.Matcher
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.random.Random

@WebMvcTest
internal class ControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `endpoint _get should return json entity with status 200 OK`() {
        val expectId = Random.nextLong()
        mockMvc.perform(MockMvcRequestBuilders.get("/get?id=${expectId}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("""{"id":${expectId},"name":"NAME"}"""))
    }
}