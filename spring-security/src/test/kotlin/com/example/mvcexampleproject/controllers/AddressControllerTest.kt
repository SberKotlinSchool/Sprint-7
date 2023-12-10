package com.example.mvcexampleproject.controllers

import com.example.mvcexampleproject.domain.Address
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class AddressControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val testAddress = Address(name = "Pam Beesly", city = "Scampton", phone = "79990000000")

    @Test
    @Order(1)
    fun `for any unauthorized user following URL should redirect to _login page`() {
        mockMvc
            .perform(get("/app/list"))
            .andExpect(status().is3xxRedirection)
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["ADMIN", "USER"])
    @Test
    @Order(2)
    fun `method get to _app_list should return empty list`() {
        assertFalse {
            sendGetToAppList().andReturn()
                .response
                .contentAsString.matches(Regex(testAddress.name!!))
        }
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["ADMIN", "USER"])
    @Test
    @Order(3)
    fun `method get to _app_add should return page with form add new address`() {
        mockMvc.perform(get("/app/add"))
            .andExpect(status().isOk)
            .andExpect(view().name("add"))
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andExpect(content().string(containsString("Add")))
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["ADMIN", "USER"])
    @Test
    @Order(4)
    fun `method post to _app_add should add new address and execute redirect`() {
        mockMvc.perform(
            post("/app/add")
                .param("name", testAddress.name)
                .param("city", testAddress.city)
                .param("phone", testAddress.phone)
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(view().name("redirect:/app/list"))

        sendGetToAppList()
            .andExpect(content().string(containsString(testAddress.name)))
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["ADMIN", "USER"])
    @Test
    @Order(5)
    fun `method get to _app_id_view should return view page`() {
        val addressId = 1L
        sendGetToAppIdView(addressId)
            .andExpect(content().string(containsString(testAddress.name)))
            .andExpect(content().string(containsString(testAddress.city)))
            .andExpect(content().string(containsString(testAddress.phone)))
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["ADMIN", "USER"])
    @Test
    @Order(6)
    fun `method get to _app_id_edit should return page edit`() {
        val addressId = 1
        mockMvc.perform(get("/app/${addressId}/edit"))
            .andExpect(status().isOk)
            .andExpect(view().name("edit"))
            .andExpect(content().string(containsString(testAddress.name)))
            .andExpect(content().string(containsString(testAddress.city)))
            .andExpect(content().string(containsString(testAddress.phone)))
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["ADMIN", "USER"])
    @Test
    @Order(7)
    fun `method post to _app_id_edit should change address and execute redirect`() {
        val newPhone = "911"
        val addressId = 1L
        mockMvc.perform(
            post("/app/${addressId}/edit")
                .param("name", testAddress.name)
                .param("city", testAddress.city)
                .param("phone", newPhone)
        )
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/app/list"))

        sendGetToAppIdView(addressId)
            .andExpect(content().string(containsString(newPhone)))
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["USER", "API"])
    @Test
    @Order(8)
    fun `method should not be available for USER and API roles`() {
        val addressId = 1L
        mockMvc.perform(post("/app/${addressId}/delete"))
            .andExpect(status().is4xxClientError)
            .andReturn().response.run {
                assertTrue { status == 403 }
            }
    }

    @WithMockUser(username = "some_user", password = "some_password", roles = ["ADMIN"])
    @Test
    @Order(9)
    fun `method post to _app_id_delete should remove address and execute redirect`() {
        val addressId = 1L
        mockMvc.perform(post("/app/${addressId}/delete"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("/app/list"))

        assertFalse {
            sendGetToAppList().andReturn()
                .response
                .contentAsString.matches(Regex(testAddress.name!!))
        }
    }


    private fun sendGetToAppList(): ResultActions = mockMvc
        .perform(get("/app/list"))
        .andExpect(status().isOk)
        .andExpect(view().name("list"))
        .andExpect(content().contentType("text/html;charset=UTF-8"))

    private fun sendGetToAppIdView(id: Long) = mockMvc
        .perform(get("/app/${id}/view"))
        .andExpect(status().isOk)
        .andExpect(view().name("view"))
        .andExpect(content().contentType("text/html;charset=UTF-8"))
}