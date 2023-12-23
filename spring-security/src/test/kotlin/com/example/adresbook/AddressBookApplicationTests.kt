package com.example.adresbook

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
class AppControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun addTestEntry() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()

        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("address", "1")
                .param("phone", "2")
        )
    }

    @WithMockUser(username = "1", password = "2", authorities = ["ROLE_APP"])
    @Test
    fun add() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/v1/add")
        )
            .andExpect(MockMvcResultMatchers.status().`is`(302))
    }

    @WithMockUser(username = "1", password = "2", authorities = ["ROLE_API"])
    @Test
    fun addFailed() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/v1/add")
        )
            .andExpect(MockMvcResultMatchers.status().`is`(403))
    }

    @WithMockUser(username = "1", password = "1", authorities = ["ROLE_API"])
    @Test
    fun list() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/list"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @WithMockUser(username = "1", password = "1", authorities = ["ROLE_APP"])
    @Test
    fun listFailed() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/list"))
            .andExpect(MockMvcResultMatchers.status().`is`(403))
    }
}