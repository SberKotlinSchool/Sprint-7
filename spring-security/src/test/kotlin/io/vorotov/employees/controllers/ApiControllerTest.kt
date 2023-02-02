package io.vorotov.employees.controllers

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
internal class ApiControllerTest {
    @LocalServerPort
    private val port: Int = 0

    @Autowired
    lateinit var mockMvc: MockMvc

    private val fullName = "Иванов Иван Иванович"

    companion object {
        var counter = 0
    }


    private fun url(path: String): String {
        return "http://localhost:${port}/api/${path}"
    }

    @Test
    @WithMockUser(username = "user", roles = ["READ"])
    fun postAddressWhenRoleIsNotRest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(url(""))
                .content("{ \"fullName\" : \"$fullName\" }")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun postAddressWhenRoleIsRest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(url(""))
                .content("{ \"fullName\" : \"$fullName\" }")
                .contentType(APPLICATION_JSON)
        )
            .also { counter++ }
            .andExpect(content().string("$counter"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "user", roles = ["READ"])
    fun getAddressesWhenRoleIsNotRest() {
        mockMvc.perform(
            get(url("view"))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun getAddressesWhenRoleIsRest() {
        addAddress()
        mockMvc.perform(
            get(url("view"))
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(counter))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "user", roles = ["READ"])
    fun getAddressWhenRoleIsNotRest() {
        mockMvc.perform(
            get(url("$counter/view"))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun getAddressWhenRoleIsRest() {
        addAddress()
        mockMvc.perform(
            get(url("$counter/view"))
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(counter))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "user", roles = ["READ"])
    fun updateAddressWhenRoleIsNotRest() {
        mockMvc.perform(
            get(url("$counter/edit"))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun editAddressWhenRoleIsRestAndAddressIsNotFound() {
        addAddress()
        mockMvc.perform(
            MockMvcRequestBuilders.put(url("${counter + 1}/edit"))
                .content("{ \"id\": ${counter + 1}, \"address\": {\"fullName\" : \"${fullName}_edited\" }}")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun editAddressWhenRoleIsRestAndAddressIsFound() {
        addAddress()
        mockMvc.perform(
            MockMvcRequestBuilders.put(url("$counter/edit"))
                .content("{ \"id\": $counter, \"address\": {\"fullName\" : \"${fullName}_edited\" }}")
                .contentType(APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.jsonPath("id").value(counter))
            .andExpect(MockMvcResultMatchers.jsonPath("fullName").value("${fullName}_edited"))
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "user", roles = ["READ"])
    fun deleteAddressWhenRoleIsNotRest() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(url("$counter/delete"))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun deleteAddressWhenRoleIsRestAndAddressIsNotFound() {
        addAddress()
        mockMvc.perform(
            MockMvcRequestBuilders.delete(url("${counter + 1}/delete"))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun deleteAddressWhenRoleIsRestAndAddressIsFound() {
        addAddress()
        mockMvc.perform(
            MockMvcRequestBuilders.delete(url("$counter/delete"))
        )
            .andExpect(status().isOk)
    }

    private fun addAddress() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(url(""))
                .content("{ \"fullName\" : \"$fullName\" }")
                .contentType(APPLICATION_JSON)
        )
        also { counter++ }
        println("counter = $counter")
    }
}