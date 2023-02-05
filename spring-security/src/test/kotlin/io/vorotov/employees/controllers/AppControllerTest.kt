package io.vorotov.employees.controllers

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(PER_CLASS)
internal class AppControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val fullName = "Иванов Иван Иванович"
    private val address = "г. Москва, Ленинский проспект, 32, кв. 18"
    private val phone = "+78000000000"
    private val email = "test@test.ru"

    var counter = 0

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun list() {
        addAddress().also { counter++ }
        checkListRequestPerform(MockMvcRequestBuilders.get("/app/list"))
        checkListRequestPerform(MockMvcRequestBuilders.get("/app/list").param("fullName", fullName))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun addAddressGetTest() {
        mockMvc.perform(MockMvcRequestBuilders.get("/app/add"))
            .andExpect(status().isOk)
            .andExpect(view().name("add"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun addCustomerPostSuccess() {
        addAddress()
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))
            .also { counter++ }

    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun deleteCustomersSuccess() {
        addAddress().also { counter++ }
        mockMvc.perform(MockMvcRequestBuilders.get("/app/$counter/delete"))
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun editAddressGetTest() {
        addAddress().also { counter++ }
        mockMvc.perform(MockMvcRequestBuilders.get("/app/$counter/edit"))
            .andExpect(status().isOk)
            .andExpect(view().name("edit"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun editAddress() {
        addAddress().also { counter++ }
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/$counter/edit")
                .param("fullName", "${fullName}_edited")
                .param("Address", address)
                .param("phone", phone)
                .param("email", email)
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/list")
        )
            .andExpect(status().isOk)
            .andExpect(content().string(Matchers.containsString("${fullName}_edited")))
    }

    private fun addAddress() =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("fullName", fullName)
                .param("address", address)
                .param("phone", phone)
                .param("email", email)
        )

    private fun checkListRequestPerform(requestBuilder: RequestBuilder) {
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
            .andExpect(content().string(Matchers.containsString(fullName)))
            .andExpect(content().string(Matchers.containsString(address)))
            .andExpect(content().string(Matchers.containsString(phone)))
            .andExpect(content().string(Matchers.containsString(email)))
    }
}