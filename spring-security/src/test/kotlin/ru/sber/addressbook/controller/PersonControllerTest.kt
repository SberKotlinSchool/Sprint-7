package ru.sber.addressbook.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
@AutoConfigureMockMvc
internal class PersonControl {
    @Autowired
    private val context: WebApplicationContext? = null
    private var mockMvc: MockMvc? = null

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context!!)
            .apply<DefaultMockMvcBuilder?>(springSecurity())
            .build()

        mockMvc!!.perform(
            post("/persons/add")
                .param("id", "0")
                .param("name", "name1")
                .param("city", "city1")
        )

        mockMvc!!.perform(
            post("/persons/add")
                .param("id", "1")
                .param("name", "name2")
                .param("city", "city2")
        )

        mockMvc!!.perform(
            post("/persons/add")
                .param("id", "2")
                .param("name", "name2")
                .param("city", "city2")
        )
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = ["ADMIN"])
    fun getPersonListByAdmin() {
        mockMvc!!.perform(get("/persons"))
            .andExpect(status().isOk)
            .andExpect(view().name("persons"))
    }

    @Test
    fun getPersonListByNonAuthorizedUser() {
        mockMvc!!.perform(get("/persons"))
            .andExpect(status().is3xxRedirection)
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = ["ADMIN"])
    fun openAddForm() {
        mockMvc!!.perform(get("/persons/add"))
            .andExpect(status().isOk)
            .andExpect(view().name("add"))
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = ["ADMIN"])
    fun add() {
        mockMvc!!.perform(
            post("/persons/add")
                .param("name", "name1")
                .param("city", "city1")
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/persons"))
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = ["ADMIN"])
    fun getById() {
        mockMvc!!.perform(get("/persons/1/view"))
            .andExpect(status().isOk)
            .andExpect(view().name("view"))
    }


    @Test
    @WithMockUser(username = "admin", password = "password", authorities = ["ADMIN"])
    fun delete() {
        mockMvc!!.perform(get("/persons/2/delete"))
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/persons"))
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = ["ADMIN"])
    fun edit() {
        mockMvc!!.perform(get("/persons/1/edit"))
            .andExpect(status().isOk)
            .andExpect(view().name("edit"))
    }

    @Test
    @WithMockUser(username = "admin", password = "password", authorities = ["ADMIN"])
    fun update() {
        mockMvc!!.perform(
            post("/persons/1/edit")
                .param("id", "1")
                .param("name", "name1")
                .param("city", "city1")
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/persons"))
    }
}