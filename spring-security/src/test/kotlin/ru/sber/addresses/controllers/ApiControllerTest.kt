package ru.sber.addresses.controllers

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun postAddressWhenRoleIsRest() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(url(""))
                .content("{ \"fullName\" : \"$fullName\" }")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .also { counter++ }
            .andExpect(MockMvcResultMatchers.content().string("$counter"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @WithMockUser(username = "user", roles = ["READ"])
    fun getAddressesWhenRoleIsNotRest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(url("view"))
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun getAddressesWhenRoleIsRest() {
        addAddress()
        mockMvc.perform(
            MockMvcRequestBuilders.get(url("view"))
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(counter))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @WithMockUser(username = "user", roles = ["READ"])
    fun getAddressWhenRoleIsNotRest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(url("$counter/view"))
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun getAddressWhenRoleIsRest() {
        addAddress()
        mockMvc.perform(
            MockMvcRequestBuilders.get(url("$counter/view"))
        )
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(counter))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @WithMockUser(username = "user", roles = ["READ"])
    fun updateAddressWhenRoleIsNotRest() {
        mockMvc.perform(
            MockMvcRequestBuilders.get(url("$counter/edit"))
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun editAddressWhenRoleIsRestAndAddressIsNotFound() {
        addAddress()
        mockMvc.perform(
            MockMvcRequestBuilders.put(url("${counter + 1}/edit"))
                .content("{ \"id\": ${counter + 1}, \"address\": {\"fullName\" : \"${fullName}_edited\" }}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun editAddressWhenRoleIsRestAndAddressIsFound() {
        addAddress()
        mockMvc.perform(
            MockMvcRequestBuilders.put(url("$counter/edit"))
                .content("{ \"id\": $counter, \"address\": {\"fullName\" : \"${fullName}_edited\" }}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.jsonPath("id").value(counter))
            .andExpect(MockMvcResultMatchers.jsonPath("fullName").value("${fullName}_edited"))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @WithMockUser(username = "user", roles = ["READ"])
    fun deleteAddressWhenRoleIsNotRest() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete(url("$counter/delete"))
        )
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun deleteAddressWhenRoleIsRestAndAddressIsNotFound() {
        addAddress()
        mockMvc.perform(
            MockMvcRequestBuilders.delete(url("${counter + 1}/delete"))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @WithMockUser(username = "admin", roles = ["REST"])
    fun deleteAddressWhenRoleIsRestAndAddressIsFound() {
        addAddress()
        mockMvc.perform(
            MockMvcRequestBuilders.delete(url("$counter/delete"))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    private fun addAddress() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(url(""))
                .content("{ \"fullName\" : \"$fullName\" }")
                .contentType(MediaType.APPLICATION_JSON)
        )
        also { counter++ }
        println("counter = $counter")
    }
}