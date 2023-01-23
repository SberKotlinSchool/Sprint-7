package ru.sber.addressbook.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
internal class PublicControllerTest {
    @Autowired
    private val context: WebApplicationContext? = null
    private var mockMvc: MockMvc? = null

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context!!)
            .apply<DefaultMockMvcBuilder?>(SecurityMockMvcConfigurers.springSecurity())
            .build()
    }

    @Test
    @WithMockUser(username = "admin", password = "password", roles = ["ADMIN"])
    fun listByAdmin() {
        mockMvc!!.perform(MockMvcRequestBuilders.get("/public/list"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun listByNonAuthorizedUser() {
        mockMvc!!.perform(MockMvcRequestBuilders.get("/public/list"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }
}