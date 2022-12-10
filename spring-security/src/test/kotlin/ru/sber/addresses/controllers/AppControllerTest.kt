package ru.sber.addresses.controllers

import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AppControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val fullName = "Иванов Иван Иванович"
    private val postAddress = "г. Москва, Ленинский проспект, 32, кв. 18"
    private val phoneNumber = "+78000000000"
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
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("add"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun addCustomerPostSuccess() {
        addAddress()
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))
            .also { counter++ }

    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun deleteCustomersSuccess() {
        addAddress().also { counter++ }
        mockMvc.perform(MockMvcRequestBuilders.get("/app/$counter/delete"))
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun editAddressGetTest() {
        addAddress().also { counter++ }
        mockMvc.perform(MockMvcRequestBuilders.get("/app/$counter/edit"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("edit"))
    }

    @Test
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    fun editAddress() {
        addAddress().also { counter++ }
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/$counter/edit")
                .param("fullName", "${fullName}_edited")
                .param("postAddress", postAddress)
                .param("phoneNumber", phoneNumber)
                .param("email", email)
        )
            .andExpect(MockMvcResultMatchers.status().is3xxRedirection)
            .andExpect(MockMvcResultMatchers.view().name("redirect:/app/list"))

        mockMvc.perform(
            MockMvcRequestBuilders.get("/app/list")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("${fullName}_edited")))
    }

    private fun addAddress() =
        mockMvc.perform(
            MockMvcRequestBuilders.post("/app/add")
                .param("fullName", fullName)
                .param("postAddress", postAddress)
                .param("phoneNumber", phoneNumber)
                .param("email", email)
        )

    private fun checkListRequestPerform(requestBuilder: RequestBuilder) {
        mockMvc.perform(requestBuilder)
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name("list"))
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(fullName)))
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(postAddress)))
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(phoneNumber)))
            .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(email)))
    }
}